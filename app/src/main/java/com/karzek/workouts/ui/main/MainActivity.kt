package com.karzek.workouts.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.karzek.core.ui.BaseActivity
import com.karzek.core.util.replaceFragment
import com.karzek.exercises.ui.overview.ExercisesFragment
import com.karzek.workouts.R.layout

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        showExerciseList()
    }

    private fun showExerciseList() {
        replaceFragment(ExercisesFragment())
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
