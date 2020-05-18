package com.karzek.workouts.ui.splash

import android.os.Bundle
import com.karzek.core.ui.BaseActivity
import com.karzek.workouts.ui.main.MainActivity

class SplashActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(MainActivity.newIntent(this))
        finish()
    }
}