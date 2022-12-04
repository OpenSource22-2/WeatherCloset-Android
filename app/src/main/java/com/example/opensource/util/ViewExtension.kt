package com.example.opensource.util

import android.util.Log
import android.view.View

inline fun View.setOnSinglePostClickListener(
    delay: Long = 3_000L,
    crossinline block: (View) -> Unit
) {
    var previousClickedTime = 0L
    setOnClickListener { view ->
        val clickedTime = System.currentTimeMillis()
        if (clickedTime - previousClickedTime >= delay) {
            block(view)
            Log.d("setOnSingleClickListener", (clickedTime - previousClickedTime).toString())
            previousClickedTime = clickedTime
        }
    }
}
