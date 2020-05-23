package com.karzek.exercises.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.karzek.core.ui.BaseActivity
import com.karzek.exercises.R
import com.karzek.exercises.domain.exercise.model.Exercise
import com.karzek.exercises.ui.detail.adapter.ExerciseImagesAdapter
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDispose
import kotlinx.android.synthetic.main.activity_exercise_details.exerciseDescription
import kotlinx.android.synthetic.main.activity_exercise_details.exerciseEquipment
import kotlinx.android.synthetic.main.activity_exercise_details.exerciseImagesList
import kotlinx.android.synthetic.main.activity_exercise_details.exerciseMuscles
import kotlinx.android.synthetic.main.activity_exercise_details.loadingView
import kotlinx.android.synthetic.main.activity_exercise_details.root
import kotlinx.android.synthetic.main.activity_exercise_details.toolbar

class ExerciseDetailsActivity : BaseActivity() {

    private lateinit var adapter: ExerciseImagesAdapter

    private val viewModel: ExerciseDetailsViewModel by bindViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_details)

        setupRecyclerView()
        setupToolbar()
        subscribeToViewModel()
        viewModel.setExercise(intent.getParcelableExtra(EXTRA_EXERCISE) ?: throw IllegalStateException("an exercise needs to be passed"))
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        exerciseImagesList.layoutManager = layoutManager
        adapter = ExerciseImagesAdapter()
        exerciseImagesList.adapter = adapter
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun subscribeToViewModel() {
        viewModel.loading
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe { isLoading ->
                if (isLoading) {
                    loadingView.visibility = View.VISIBLE
                } else {
                    loadingView.visibility = View.GONE
                }
            }

        viewModel.error
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe {
                Snackbar.make(root, R.string.error_on_loading_images, Snackbar.LENGTH_INDEFINITE).apply {
                        setAction(R.string.generic_refresh) { viewModel.loadImages() }
                        show()
                    }
            }

        viewModel.exerciseDetails
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe { exercise ->
                displayExerciseDetails(exercise)
            }

        viewModel.exerciseImages
            .autoDispose(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe { imageUrls ->
                if (!imageUrls.isNullOrEmpty()) {
                    exerciseImagesList.visibility = View.VISIBLE
                    adapter.setData(imageUrls)
                }
            }
    }

    private fun displayExerciseDetails(
        exercise: Exercise
    ) {
        supportActionBar?.run {
            title = exercise.name
            subtitle = exercise.category.name
        }
        if (!exercise.description.isNullOrEmpty() && exercise.description.length > 3) {
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