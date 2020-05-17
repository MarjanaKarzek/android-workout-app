package com.karzek.exercises.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.karzek.exercises.R
import com.karzek.exercises.domain.model.Exercise

class ExercisesAdapter(
    private val interactionListener: ExerciseInteractionListener
) : RecyclerView.Adapter<ExerciseViewHolder>() {

    private val data = ArrayList<Exercise>()

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
    ) = holder.bind(data[position])

    fun addData(data: List<Exercise>) {
        val originalItemCount = this.data.size
        this.data.addAll(data)
        notifyItemRangeInserted(originalItemCount, data.size)
    }

    override fun getItemCount() = data.size

}