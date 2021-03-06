package com.karzek.exercises.ui.overview.adapter

import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.RecyclerView
import com.karzek.core.ui.images.GlideApp
import com.karzek.exercises.R
import com.karzek.exercises.domain.exercise.model.Exercise
import kotlinx.android.synthetic.main.view_holder_exercise.view.divider
import kotlinx.android.synthetic.main.view_holder_exercise.view.exerciseCategory
import kotlinx.android.synthetic.main.view_holder_exercise.view.exerciseEquipment
import kotlinx.android.synthetic.main.view_holder_exercise.view.exerciseImage
import kotlinx.android.synthetic.main.view_holder_exercise.view.exerciseMuscles
import kotlinx.android.synthetic.main.view_holder_exercise.view.exerciseName

class ExerciseViewHolder(
    private val containerView: View,
    private val interactionListener: ExerciseInteractionListener
) : RecyclerView.ViewHolder(containerView), OnClickListener {

    private lateinit var originalItem: Exercise

    init {
        containerView.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (adapterPosition == RecyclerView.NO_POSITION) return

        interactionListener.onExerciseClicked(originalItem)
    }

    fun bind(item: Exercise) = with(containerView) {
        originalItem = item
        exerciseName.text = originalItem.name
        exerciseCategory.text = originalItem.category.name

        loadThumbnail()
        setupEquipmentSection()
        setupMuscleSection()
        setupDivider()
    }

    private fun loadThumbnail() = with(containerView) {
        if(originalItem.imageThumbnailUrl != null){
            GlideApp.with(containerView)
                .load(originalItem.imageThumbnailUrl)
                .into(exerciseImage)
        } else {
            exerciseImage.setImageResource(R.drawable.ic_exercise_placeholder)
        }
    }

    private fun setupEquipmentSection() = with(containerView) {
        if (originalItem.equipment.isNullOrEmpty()) {
            exerciseEquipment.visibility = View.GONE
        } else {
            exerciseEquipment.visibility = View.VISIBLE
            exerciseEquipment.text = originalItem.equipment!!.joinToString(", ")
        }
    }

    private fun setupMuscleSection() = with(containerView) {
        if (originalItem.muscles.isNullOrEmpty()) {
            exerciseMuscles.visibility = View.GONE
        } else {
            exerciseMuscles.visibility = View.VISIBLE
            exerciseMuscles.text = originalItem.muscles!!.joinToString(", ")
        }
    }

    private fun setupDivider() = with(containerView) {
        if (originalItem.equipment.isNullOrEmpty() && originalItem.muscles.isNullOrEmpty()) {
            divider.visibility = View.GONE
        } else {
            divider.visibility = View.VISIBLE
        }
    }
}