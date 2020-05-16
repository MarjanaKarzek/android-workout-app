package com.karzek.core.util

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.karzek.core.ui.BaseFragment

inline fun FragmentActivity.replaceFragment(
    fragment: BaseFragment,
    addToBackStack: Boolean = false,
    animationBlock: FragmentTransaction.() -> Unit = {}
) {
    supportFragmentManager.beginTransaction()
        .apply(animationBlock)
        .replace(android.R.id.content, fragment, fragment.getTagForStack())
        .also { if (addToBackStack) it.addToBackStack(null) }
        .commit()
}

inline fun FragmentActivity.addFragment(
    fragment: BaseFragment,
    animationBlock: FragmentTransaction.() -> Unit = {}
) {
    supportFragmentManager.beginTransaction()
        .apply(animationBlock)
        .add(android.R.id.content, fragment, fragment.getTagForStack())
        .addToBackStack(null)
        .commit()
}