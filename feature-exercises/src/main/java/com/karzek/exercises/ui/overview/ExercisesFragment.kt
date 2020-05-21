package com.karzek.exercises.ui.overview

import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.jakewharton.rxbinding3.appcompat.itemClicks
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import com.jakewharton.rxbinding3.view.clicks
import com.karzek.core.ui.BaseFragment
import com.karzek.core.ui.binding.checkedChanges
import com.karzek.exercises.R
import com.karzek.exercises.domain.category.model.Category
import com.karzek.exercises.domain.exercise.model.Exercise
import com.karzek.exercises.domain.exercise.model.LoadingState.Error
import com.karzek.exercises.domain.exercise.model.LoadingState.Loading
import com.karzek.exercises.domain.exercise.model.LoadingState.Success
import com.karzek.exercises.ui.detail.ExerciseDetailsActivity
import com.karzek.exercises.ui.overview.adapter.ExerciseInteractionListener
import com.karzek.exercises.ui.overview.adapter.ExercisesAdapter
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDispose
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.component_error_view_with_action.errorViewAction
import kotlinx.android.synthetic.main.fragment_exercises.errorView
import kotlinx.android.synthetic.main.fragment_exercises.exerciseFilterOptions
import kotlinx.android.synthetic.main.fragment_exercises.loadingView
import kotlinx.android.synthetic.main.fragment_exercises.recyclerView
import kotlinx.android.synthetic.main.fragment_exercises.toolbar
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ExercisesFragment : BaseFragment(R.layout.fragment_exercises), ExerciseInteractionListener {

    private val viewModel: ExercisesViewModel by bindViewModel()
    private lateinit var adapter: ExercisesAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var searchView: SearchView

    private val filterChips = mutableListOf<Chip>()

    override fun getTagForStack() = ExercisesFragment::class.java.toString()

    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
    ) {
        requireActivity().menuInflater.inflate(R.menu.menu_exercises, menu)
        searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.queryHint = getString(R.string.exercise_search_hint)
        searchView.queryTextChanges()
            .skipInitialValue()
            .debounce(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe {
                Timber.d("sending query $it")
                viewModel.setQueryFilter(it)
            }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupToolbar()
        subscribeToViewModel()
        subscribeToLayout()
    }

    private fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = SlideInUpAnimator()
    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        toolbar.itemClicks()
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe {
                if (it.itemId == R.id.filter) {
                    if (exerciseFilterOptions.visibility == View.GONE) {
                        exerciseFilterOptions.visibility = View.VISIBLE
                    } else {
                        exerciseFilterOptions.visibility = View.GONE
                    }
                }
            }
    }

    private fun subscribeToViewModel() {
        viewModel.error
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe {
                setLoading(false)
                if (adapter.itemCount == 0) {
                    errorView.visibility = View.VISIBLE
                } else {
                    Toast.makeText(requireContext(), R.string.error_unknown, Toast.LENGTH_LONG).show()
                }
            }

        viewModel.filterOptions
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe {
                if (it.isNotEmpty()) {
                    filterChips.clear()
                    exerciseFilterOptions.removeAllViews()
                    it.forEach { category ->
                        val chip = Chip(exerciseFilterOptions.context)
                        chip.tag = category
                        chip.text = category.name
                        chip.isClickable = true
                        chip.isCheckable = true
                        filterChips.add(chip)
                        exerciseFilterOptions.addView(chip)
                    }
                }
            }

        viewModel.initialized
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe {
                loadingView.visibility = View.GONE
                initExerciseList()
            }

        viewModel.loadingStatus
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe { state ->
                when (state) {
                    is Loading -> {
                        setFilterOptionsEnabled(false)
                        setSearchViewEnabled(false)
                        setLoading(true)
                    }
                    is Success -> {
                        setFilterOptionsEnabled(true)
                        setSearchViewEnabled(true)
                        setLoading(false)
                    }
                    is Error -> {
                        setLoading(false)
                        if (adapter.itemCount == 0) {
                            errorView.visibility = View.VISIBLE
                        } else {
                            Toast.makeText(requireContext(), R.string.error_network_on_loading_exercises, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
    }

    private fun setFilterOptionsEnabled(isEnabled: Boolean) {
        filterChips.forEach {
            it.isEnabled = isEnabled
        }
    }

    private fun setSearchViewEnabled(isEnabled: Boolean) {
        searchView.isEnabled = isEnabled
        if (!isEnabled) {
            searchView.clearFocus()
            searchView.inputType = InputType.TYPE_NULL
        } else {
            searchView.requestFocus()
            searchView.inputType = InputType.TYPE_CLASS_TEXT
        }
    }

    private fun initExerciseList() {
        adapter = ExercisesAdapter(this)
        recyclerView.adapter = adapter
        viewModel.exercises
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe { exercises ->
                adapter.submitList(exercises)
                if (exercises.isNotEmpty()) {
                    errorView.visibility = View.GONE
                }
            }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            loadingView.visibility = View.VISIBLE
        } else {
            loadingView.visibility = View.GONE
        }
    }

    private fun subscribeToLayout() {
        errorViewAction.clicks()
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe {
                errorView.visibility = View.GONE
                viewModel.initialize()
            }

        exerciseFilterOptions.checkedChanges()
            .skip(1)
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe {
                val selectedCategory = exerciseFilterOptions.findViewById<Chip?>(it)?.tag as Category?
                viewModel.setCategoryFilter(selectedCategory?.id)
            }
    }

    override fun onExerciseClicked(exercise: Exercise) {
        startActivity(ExerciseDetailsActivity.newIntent(requireContext(), exercise))
    }

}