package com.karzek.exercises.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.karzek.exercises.R

class ExerciseImagesAdapter : ListAdapter<String, ExerciseImageViewHolder>(ImageUrlDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExerciseImageViewHolder {
        return ExerciseImageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_exercise_image, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ExerciseImageViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    fun setData(data: List<String>) {
        submitList(data)
    }

}