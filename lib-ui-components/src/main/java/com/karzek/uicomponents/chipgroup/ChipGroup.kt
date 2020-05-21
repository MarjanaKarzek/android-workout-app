package com.karzek.uicomponents.chipgroup

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ChipGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.chipGroupStyle
) : ChipGroup(context, attrs, defStyleAttr) {

    private val chips = mutableListOf<Chip>()

    fun setChipTags(options: List<Pair<String, Any>>) {
        chips.clear()
        removeAllViews()
        options.forEach { option ->
            val chip = Chip(context)
            chip.text = option.first
            chip.tag = option.second
            chip.isClickable = true
            chip.isCheckable = true
            chips.add(chip)
            addView(chip)
        }
    }

    fun enableChips() {
        chips.forEach {
            it.isEnabled = true
        }
    }

    fun disableChips() {
        chips.forEach {
            it.isEnabled = false
        }
    }

}