package com.karzek.exercises.ui.adapter

import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.RecyclerView
import com.karzek.exercises.domain.model.Exercise
import kotlinx.android.synthetic.main.view_holder_exercise.view.exerciseCategory
import kotlinx.android.synthetic.main.view_holder_exercise.view.exerciseEquipment
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
        exerciseCategory.text = originalItem.category

        //TODO load image from url
        setupEquipmentSection()
        setupMuscleSection()
    }

    private fun setupEquipmentSection() = with(containerView) {
        originalItem.equipment?.run {
            exerciseEquipment.visibility = View.VISIBLE
            exerciseEquipment.text = this.joinToString(", ")
        } ?: run {
            exerciseEquipment.visibility = View.GONE
        }
    }

    private fun setupMuscleSection() = with(containerView) {
        originalItem.muscles?.run {
            exerciseMuscles.visibility = View.VISIBLE
            exerciseMuscles.text = this.joinToString(", ")
        } ?: run {
            exerciseMuscles.visibility = View.GONE
        }
    }
}