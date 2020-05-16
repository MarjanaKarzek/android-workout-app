package com.karzek.exercises.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.karzek.exercises.R
import com.karzek.exercises.domain.model.Exercise

class ExercisesAdapter(
    private val interactionListener: ExerciseInteractionListener
) : ListAdapter<Exercise, ExerciseViewHolder>(ExerciseDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ExerciseViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_exercise, parent, false), interactionListener
    )

    override fun onBindViewHolder(
        holder: ExerciseViewHolder,
        position: Int
    ) = holder.bind(getItem(position))

    fun swapData(data: List<Exercise>) {
        submitList(data.toMutableList())
    }

}