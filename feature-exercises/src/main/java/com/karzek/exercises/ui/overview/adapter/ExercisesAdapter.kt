package com.karzek.exercises.ui.overview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.karzek.exercises.R.layout
import com.karzek.exercises.domain.category.model.Category
import com.karzek.exercises.domain.exercise.model.Exercise
import com.karzek.exercises.ui.overview.adapter.ExerciseAdapterViewType.EXERCISE
import com.karzek.exercises.ui.overview.adapter.ExerciseAdapterViewType.LOADING
import com.karzek.exercises.ui.overview.adapter.viewholder.ExerciseViewHolder
import com.karzek.exercises.ui.overview.adapter.viewholder.LoadingViewHolder
import java.util.Locale

class ExercisesAdapter(
    private val interactionListener: ExerciseInteractionListener
) : RecyclerView.Adapter<ViewHolder>(), Filterable {

    private var lastSearch: CharSequence? = null
    private var lastSelectedCategory: Category? = null
    private var isLoading = false

    private val allData = ArrayList<Exercise>()
    private val data = ArrayList<Exercise>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return if (viewType == EXERCISE.ordinal) {
            ExerciseViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(layout.view_holder_exercise, parent, false),
                interactionListener
            )
        } else {
            LoadingViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(layout.view_holder_loading, parent, false)
            )
        }
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        when (holder) {
            is ExerciseViewHolder -> holder.bind(data[position])
            else -> {
                //do nothing
            }
        }
    }

    fun initData(data: List<Exercise>) {
        allData.addAll(data)
        this.data.addAll(data)
        notifyItemRangeInserted(0, data.size)
    }

    fun addData(data: List<Exercise>) {
        allData.addAll(data)
        filter.filter(lastSearch)
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading && position == data.size) {
            LOADING.ordinal
        } else {
            EXERCISE.ordinal
        }
    }

    override fun getItemCount(): Int {
        return if (isLoading) {
            data.size + 1
        } else {
            data.size
        }
    }

    fun getTotalItemCount() = allData.size

    fun setLoading(isLoading: Boolean) {
        this.isLoading = isLoading
        notifyDataSetChanged()
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

        @Suppress("UNCHECKED_CAST")
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

    override fun getFilter(): Filter {
        return exerciseFilter
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