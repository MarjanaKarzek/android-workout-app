package com.karzek.exercises.ui.detail.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.karzek.core.ui.images.GlideApp
import com.karzek.exercises.R
import kotlinx.android.synthetic.main.view_holder_exercise_image.view.exerciseImage

class ExerciseImageViewHolder(
    private val containerView: View
) : RecyclerView.ViewHolder(containerView) {

    fun bind(item: String) = with(containerView) {
        GlideApp.with(this)
            .load(item)
            .placeholder(R.drawable.ic_exercise_placeholder)
            .into(exerciseImage)
    }

}