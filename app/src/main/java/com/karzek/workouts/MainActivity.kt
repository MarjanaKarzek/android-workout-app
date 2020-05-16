package com.karzek.workouts

import android.os.Bundle
import com.karzek.core.ui.BaseActivity
import com.karzek.core.util.replaceFragment
import com.karzek.exercises.ui.ExercisesFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showExerciseList()
    }

    private fun showExerciseList(){
        replaceFragment(ExercisesFragment())
    }
}
