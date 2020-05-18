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
import com.jakewharton.rxbinding3.appcompat.itemClicks
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import com.jakewharton.rxbinding3.recyclerview.scrollEvents
import com.karzek.core.ui.BaseFragment
import com.karzek.exercises.R
import com.karzek.exercises.domain.model.Exercise
import com.karzek.exercises.ui.detail.ExerciseDetailsActivity
import com.karzek.exercises.ui.overview.adapter.ExerciseInteractionListener
import com.karzek.exercises.ui.overview.adapter.ExercisesAdapter
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDispose
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.fragment_exercises.recyclerView
import kotlinx.android.synthetic.main.fragment_exercises.toolbar

class ExercisesFragment : BaseFragment(R.layout.fragment_exercises), ExerciseInteractionListener {

    private val viewModel: ExercisesViewModel by bindViewModel()
    private lateinit var adapter: ExercisesAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun getTagForStack() = ExercisesFragment::class.java.toString()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupToolbar()
        subscribeToViewModel()
        viewModel.getInitialExercises()
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
                val visibleItemCount = layoutManager.childCount
                val filteredItemCount = layoutManager.itemCount
                val totalItemCount = adapter.getTotalItemCount()
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                viewModel.onScroll(visibleItemCount, filteredItemCount, totalItemCount, firstVisibleItemPosition)
            }

    }

    private fun subscribeToViewModel() {
        viewModel.exercises
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe { exercises ->
                if (adapter.getTotalItemCount() == 0) {
                    adapter.initData(exercises)
                } else {
                    adapter.addData(exercises)
                    adapter.applyLastFilter()
                }
            }
    }

    override fun onExerciseClicked(exercise: Exercise) {
        startActivity(ExerciseDetailsActivity.newIntent(requireContext(), exercise))
    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        toolbar.itemClicks()
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe {
                if (it.itemId == R.id.filter) {
                    //TODO show filter
                    Toast.makeText(context, "On filter clicked", Toast.LENGTH_SHORT).show()
                }
            }
    }

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
            }
    }
}