package com.crysure.reflvy.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebView

class NonScrollWebView : WebView {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Memproses event sentuhan tetapi mengabaikan event scroll
        return if (event.action == MotionEvent.ACTION_MOVE) {
            false
        } else super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        // Mengabaikan event intercept
        return false
    }
}