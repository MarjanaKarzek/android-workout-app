package com.karzek.exercises.ui.overview.adapter

import androidx.recyclerview.widget.DiffUtil
import com.karzek.exercises.domain.exercise.model.Exercise

class ExerciseDiffUtil : DiffUtil.ItemCallback<Exercise>() {

    override fun areItemsTheSame(
        oldItem: Exercise,
        newItem: Exercise
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Exercise,
        newItem: Exercise
    ): Boolean {
        return oldItem == newItem
    }
}