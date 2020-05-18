package com.karzek.exercises.ui.overview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.karzek.exercises.R
import com.karzek.exercises.domain.category.model.Category
import com.karzek.exercises.domain.exercise.model.Exercise
import java.util.Locale

class ExercisesAdapter(
    private val interactionListener: ExerciseInteractionListener
) : RecyclerView.Adapter<ExerciseViewHolder>(), Filterable {

    private var lastSearch: CharSequence? = null
    private var lastSelectedCategory: Category? = null

    private val allData = ArrayList<Exercise>()
    private val data = ArrayList<Exercise>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ExerciseViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_exercise, parent, false),
        interactionListener
    )

    override fun onBindViewHolder(
        holder: ExerciseViewHolder,
        position: Int
    ) = holder.bind(data[position])

    fun initData(data: List<Exercise>) {
        allData.addAll(data)
        this.data.addAll(data)
        notifyItemRangeInserted(0, data.size)
    }

    fun addData(data: List<Exercise>) {
        allData.addAll(data)
    }

    override fun getItemCount() = data.size

    fun getTotalItemCount() = allData.size

    override fun getFilter(): Filter {
        return exerciseFilter
    }

    fun applyLastFilter() {
        this.filter.filter(lastSearch)
    }

    private val exerciseFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            lastSearch = constraint

            var filteredList = applySearchFilter(constraint)

            lastSelectedCategory?.run {
                filteredList = filteredList.filter { it.category == name }
            }

            val result = FilterResults()
            result.values = filteredList
            return result
        }

        override fun publishResults(
            constraint: CharSequence?,
            results: FilterResults?
        ) {
            data.clear()
            results?.run {
                data.addAll(values as List<Exercise>)
                notifyDataSetChanged()
            }
        }

    }

    private fun applySearchFilter(constraint: CharSequence?): List<Exercise> {
        return if (constraint.isNullOrBlank()) {
            allData
        } else {
            val filterPattern = constraint.toString().toLowerCase(Locale.ENGLISH).trim()
            allData.filter { it.name.toLowerCase(Locale.ENGLISH).contains(filterPattern) }
        }
    }

    fun setCategoryFilter(category: Category?) {
        lastSelectedCategory = category
        filter.filter(lastSearch)
    }

}