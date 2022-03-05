package com.mradzinski.probablelamp.common.extension

fun <T> List<T>.copy(): List<T> = ArrayList<T>(this.count()).apply {
    addAll(this@copy)
}