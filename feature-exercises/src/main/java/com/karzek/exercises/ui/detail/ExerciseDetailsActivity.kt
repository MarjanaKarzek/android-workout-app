package com.karzek.exercises.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Lifecycle
import com.karzek.core.ui.BaseActivity
import com.karzek.exercises.R
import com.karzek.exercises.domain.model.Exercise
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDispose
import kotlinx.android.synthetic.main.activity_exercise_details.exerciseDescription
import kotlinx.android.synthetic.main.activity_exercise_details.exerciseEquipment
import kotlinx.android.synthetic.main.activity_exercise_details.exerciseMuscles
import kotlinx.android.synthetic.main.activity_exercise_details.toolbar

class ExerciseDetailsActivity : BaseActivity() {

    private val viewModel: ExerciseDetailsViewModel by bindViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_details)

        setupToolbar()
        subscribeToViewModel()
        viewModel.setExercise(intent.getParcelableExtra(EXTRA_EXERCISE) ?: throw IllegalStateException("an exercise needs to be passed"))
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun subscribeToViewModel() {
        viewModel.exerciseDetails
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe { (exercise, imageUrls) ->
                supportActionBar?.run {
                    title = exercise.name
                    subtitle = exercise.category
                }
                if (!exercise.description.isNullOrEmpty()) {
                    exerciseDescription.visibility = View.VISIBLE
                    exerciseDescription.text = Html.fromHtml(exercise.description, Html.FROM_HTML_MODE_COMPACT)
                }
                if (!exercise.muscles.isNullOrEmpty()) {
                    exerciseMuscles.visibility = View.VISIBLE
                    exerciseMuscles.text = exercise.muscles.joinToString("\n")
                }
                if (!exercise.equipment.isNullOrEmpty()) {
                    exerciseEquipment.visibility = View.VISIBLE
                    exerciseEquipment.text = exercise.equipment.joinToString("\n")
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    companion object {

        private const val EXTRA_EXERCISE = "EXTRA_EXERCISE"

        fun newIntent(
            context: Context,
            exercise: Exercise
        ): Intent {
            return Intent(context, ExerciseDetailsActivity::class.java).apply {
                putExtra(EXTRA_EXERCISE, exercise)
            }
        }
    }
}