package com.karzek.exercises.ui

import com.karzek.core.ui.BaseFragment
import com.karzek.exercises.R

class ExercisesFragment : BaseFragment(R.layout.fragment_exercise_list) {

    private val viewModel: ExercisesViewModel by bindViewModel()

    override fun getTagForStack() = ExercisesFragment::class.java.toString()

}