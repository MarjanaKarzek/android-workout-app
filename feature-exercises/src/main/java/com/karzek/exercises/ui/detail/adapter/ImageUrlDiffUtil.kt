package com.karzek.exercises.ui.detail.adapter

import androidx.recyclerview.widget.DiffUtil

class ImageUrlDiffUtil : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem == newItem
    }
}