package com.karzek.exercises.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.karzek.core.ui.BaseFragment
import com.karzek.core.ui.binding.itemClicks
import com.karzek.exercises.R
import com.karzek.exercises.domain.model.Exercise
import com.karzek.exercises.ui.adapter.ExerciseInteractionListener
import com.karzek.exercises.ui.adapter.ExercisesAdapter
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDispose
import kotlinx.android.synthetic.main.fragment_exercises.recyclerView
import kotlinx.android.synthetic.main.fragment_exercises.toolbar

class ExercisesFragment : BaseFragment(R.layout.fragment_exercises), ExerciseInteractionListener {

    private val viewModel: ExercisesViewModel by bindViewModel()
    private lateinit var adapter: ExercisesAdapter

    override fun getTagForStack() = ExercisesFragment::class.java.toString()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupToolbar()
        subscribeToViewModel()
        viewModel.getExercises()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ExercisesAdapter(this)
        recyclerView.adapter = adapter
    }

    private fun subscribeToViewModel() {
        viewModel.exercises
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe { exercises ->
                adapter.swapData(exercises)
            }
    }

    override fun onExerciseClicked(exercise: Exercise) {
        //TODO show exercise details
        Toast.makeText(context, "On exercise \"${exercise.name} clicked", Toast.LENGTH_SHORT).show()
    }

    private fun setupToolbar() {
        toolbar.itemClicks()
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe {
                when (it.itemId) {
                    R.id.search -> {
                        //TODO show search
                        Toast.makeText(context, "On search clicked", Toast.LENGTH_SHORT).show()
                    }
                    R.id.filter -> {
                        //TODO show filter
                        Toast.makeText(context, "On filter clicked", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

}