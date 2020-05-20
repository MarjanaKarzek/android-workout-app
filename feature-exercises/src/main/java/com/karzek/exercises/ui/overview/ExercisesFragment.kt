package com.karzek.exercises.ui.overview

import android.os.Bundle
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
import com.jakewharton.rxbinding3.recyclerview.scrollEvents
import com.jakewharton.rxbinding3.view.clicks
import com.karzek.core.ui.BaseFragment
import com.karzek.core.ui.binding.checkedChanges
import com.karzek.exercises.R
import com.karzek.exercises.domain.category.model.Category
import com.karzek.exercises.domain.exercise.model.Exercise
import com.karzek.exercises.ui.detail.ExerciseDetailsActivity
import com.karzek.exercises.ui.overview.adapter.ExerciseInteractionListener
import com.karzek.exercises.ui.overview.adapter.ExercisesAdapter
import com.karzek.exercises.ui.overview.error.NetworkErrorOnLoadingItems
import com.karzek.exercises.ui.overview.error.NetworkErrorOnViewInit
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDispose
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.component_error_view_with_action.errorViewAction
import kotlinx.android.synthetic.main.fragment_exercises.errorView
import kotlinx.android.synthetic.main.fragment_exercises.exerciseFilterOptions
import kotlinx.android.synthetic.main.fragment_exercises.loadingView
import kotlinx.android.synthetic.main.fragment_exercises.recyclerView
import kotlinx.android.synthetic.main.fragment_exercises.toolbar

class ExercisesFragment : BaseFragment(R.layout.fragment_exercises), ExerciseInteractionListener {

    private val viewModel: ExercisesViewModel by bindViewModel()
    private lateinit var adapter: ExercisesAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var firstVisibleInList = 0

    override fun getTagForStack() = ExercisesFragment::class.java.toString()

    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
    ) {
        requireActivity().menuInflater.inflate(R.menu.menu_exercises, menu)
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.queryHint = getString(R.string.exercise_search_hint)
        searchView.queryTextChanges()
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe {
                adapter.filter.filter(it)
                checkForMoreItems()
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
        viewModel.retrieveInitialData()
        subscribeToLayout()
    }

    private fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        adapter = ExercisesAdapter(this)
        recyclerView.itemAnimator = SlideInUpAnimator()
        recyclerView.adapter = adapter
        recyclerView.scrollEvents()
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe {
                checkForMoreItems()
            }
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
        viewModel.viewLoading
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe { isLoading ->
                if (isLoading) {
                    loadingView.visibility = View.VISIBLE
                } else {
                    loadingView.visibility = View.GONE
                }
            }

        viewModel.listLoading
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe { isLoading ->
                //todo investigate false loading indication on filter
                adapter.setLoading(isLoading)
            }

        viewModel.error
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe { error ->
                when (error) {
                    is NetworkErrorOnViewInit -> showErrorView()
                    is NetworkErrorOnLoadingItems -> {
                        if (adapter.getTotalItemCount() == 0) {
                            showErrorView()
                        } else {
                            Toast.makeText(
                                requireContext(), R.string.error_network_on_loading_exercises, Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    else -> Toast.makeText(requireContext(), R.string.error_unknown, Toast.LENGTH_LONG).show()
                }

            }


        viewModel.filterOptions
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe {
                if (it.isNotEmpty()) {
                    it.forEach { category ->
                        val chip = Chip(exerciseFilterOptions.context)
                        chip.tag = category
                        chip.text = category.name
                        chip.isClickable = true
                        chip.isCheckable = true
                        exerciseFilterOptions.addView(chip)
                    }
                }
            }

        viewModel.exercises
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe { exercises ->
                if (adapter.getTotalItemCount() == 0) {
                    adapter.initData(exercises)
                } else {
                    adapter.addData(exercises)
                    checkForMoreItems()
                }
            }
    }

    private fun subscribeToLayout() {
        errorViewAction.clicks()
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe {
                errorView.visibility = View.GONE
                viewModel.retrieveInitialData()
            }

        exerciseFilterOptions.checkedChanges()
            .skip(1)
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe {
                val selectedCategory = exerciseFilterOptions.findViewById<Chip?>(it)?.tag as Category?
                adapter.setCategoryFilter(selectedCategory)
                checkForMoreItems()
            }
    }

    private fun showErrorView() {
        errorView.visibility = View.VISIBLE
    }

    private fun isScrollDirectionDown(): Boolean {
        val currentFirstVisible = layoutManager.findFirstVisibleItemPosition()

        val isScrollingDown = currentFirstVisible > firstVisibleInList
        firstVisibleInList = currentFirstVisible
        return isScrollingDown
    }

    private fun checkForMoreItems() {
        viewModel.checkForMoreItems(
            layoutManager.childCount,
            layoutManager.itemCount,
            adapter.getTotalItemCount(),
            layoutManager.findFirstVisibleItemPosition(),
            isScrollDirectionDown()
        )
    }

    override fun onExerciseClicked(exercise: Exercise) {
        startActivity(ExerciseDetailsActivity.newIntent(requireContext(), exercise))
    }

}