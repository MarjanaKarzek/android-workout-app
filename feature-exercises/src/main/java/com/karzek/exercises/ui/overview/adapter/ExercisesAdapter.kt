package com.karzek.exercises.ui.overview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.karzek.exercises.R.layout
import com.karzek.exercises.domain.exercise.model.Exercise

class ExercisesAdapter(
    private val interactionListener: ExerciseInteractionListener
) : PagedListAdapter<Exercise, ExerciseViewHolder>(ExerciseDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ExerciseViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(layout.view_holder_exercise, parent, false),
        interactionListener
    )

    override fun onBindViewHolder(
        holder: ExerciseViewHolder,
        position: Int
    ) {
        getItem(position)?.run {
            holder.bind(this)
        }
    }
}