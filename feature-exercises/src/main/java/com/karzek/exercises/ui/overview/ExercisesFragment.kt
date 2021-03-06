package com.karzek.exercises.ui.overview

import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding3.view.actionViewEvents
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.editorActions
import com.karzek.core.ui.BaseFragment
import com.karzek.core.ui.binding.checkedChanges
import com.karzek.core.util.showSoftKeyboard
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
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.exercises_error_view_with_action.errorViewAction
import kotlinx.android.synthetic.main.fragment_exercises.errorView
import kotlinx.android.synthetic.main.fragment_exercises.exerciseFilterOptions
import kotlinx.android.synthetic.main.fragment_exercises.loadingView
import kotlinx.android.synthetic.main.fragment_exercises.recyclerView
import kotlinx.android.synthetic.main.fragment_exercises.toolbar
import kotlinx.android.synthetic.main.menu_item_filter.filterBadge
import timber.log.Timber

class ExercisesFragment : BaseFragment(R.layout.fragment_exercises), ExerciseInteractionListener {

    private val viewModel: ExercisesViewModel by bindViewModel()
    private lateinit var adapter: ExercisesAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var searchView: SearchView

    override fun getTagForStack() = ExercisesFragment::class.java.toString()

    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
    ) {
        requireActivity().menuInflater.inflate(R.menu.menu_exercises, menu)

        searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.queryHint = getString(R.string.exercise_search_hint)
        searchView.imeOptions = EditorInfo.IME_ACTION_SEARCH

        setSearchViewListeners(menu)
        setFilterListener(menu)
    }

    private fun setSearchViewListeners(menu: Menu) {
        val searchEditText: EditText? = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText?
        searchEditText?.apply {
            editorActions()
                .autoDispose(AndroidLifecycleScopeProvider.from(this@ExercisesFragment, Lifecycle.Event.ON_DESTROY))
                .subscribe { action ->
                    if (action == EditorInfo.IME_ACTION_SEARCH) {
                        val query = searchEditText.text.toString()
                        Timber.d("sending query $query")
                        viewModel.setQueryFilter(query)
                    }
                }
        }
        val searchClearButton: ImageView? = searchView.findViewById(androidx.appcompat.R.id.search_close_btn) as ImageView?
        searchClearButton?.apply {
            clicks()
                .autoDispose(AndroidLifecycleScopeProvider.from(this@ExercisesFragment, Lifecycle.Event.ON_DESTROY))
                .subscribe {
                    searchEditText?.setText("")
                    searchEditText?.requestFocus()
                    activity?.showSoftKeyboard()
                }
        }
        val searchMenuItem = menu.findItem(R.id.search);
        searchMenuItem.actionViewEvents()
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe {
                //checks last state before event is executed
                if (it.menuItem.isActionViewExpanded) {
                    Timber.d("sending query empty")
                    viewModel.setQueryFilter("")
                }
            }
    }

    private fun setFilterListener(menu: Menu) {
        menu.findItem(R.id.filter).actionView.clicks()
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe {
                if (exerciseFilterOptions.visibility == View.GONE) {
                    exerciseFilterOptions.visibility = View.VISIBLE
                } else {
                    exerciseFilterOptions.visibility = View.GONE
                }
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
        adapter = ExercisesAdapter(this)
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = SlideInUpAnimator()
        recyclerView.adapter = adapter
    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
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
            .subscribe { categories ->
                if (categories.isNotEmpty()) {
                    exerciseFilterOptions.setChipTags(categories.map { Pair(it.name, it) })
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
                        exerciseFilterOptions.disableChips()
                        setSearchViewEnabled(false)
                        setLoading(true)
                    }
                    is Success -> {
                        exerciseFilterOptions.enableChips()
                        setSearchViewEnabled(true)
                        setLoading(false)
                    }
                    is Error -> {
                        exerciseFilterOptions.enableChips()
                        setSearchViewEnabled(true)
                        setLoading(false)
                        if (adapter.itemCount == 0) {
                            errorView.visibility = View.VISIBLE
                        } else {
                            Snackbar.make(view!!, R.string.error_network_on_loading_exercises, Snackbar.LENGTH_INDEFINITE)
                                .apply {
                                    setAction(R.string.generic_refresh) { viewModel.refreshUI() }
                                    show()
                                }
                        }
                    }
                }
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
                if (selectedCategory == null) {
                    filterBadge.visibility = View.GONE
                } else {
                    filterBadge.visibility = View.VISIBLE
                }
            }
    }

    override fun onExerciseClicked(exercise: Exercise) {
        startActivity(ExerciseDetailsActivity.newIntent(requireContext(), exercise))
    }

}