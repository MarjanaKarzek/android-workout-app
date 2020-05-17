package com.karzek.exercises.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.karzek.exercises.domain.model.Exercise

class ExerciseDiffUtil : DiffUtil.ItemCallback<Exercise>() {

    override fun areItemsTheSame(
        oldItem: Exercise,
        newItem: Exercise
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: Exercise,
        newItem: Exercise
    ): Boolean {
        return oldItem.name == newItem.name
    }
}