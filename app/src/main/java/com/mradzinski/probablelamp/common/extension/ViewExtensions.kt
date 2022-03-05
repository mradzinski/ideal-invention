package com.mradzinski.probablelamp.common.extension

import android.view.View

fun View.show() {
    visibility = View.VISIBLE
}

fun View.showIfElseHide(condition: Boolean, hiddenVisibility: Int = View.GONE) =
    if (condition) {
        show()
    } else {
        visibility = hiddenVisibility
    }