package com.crysure.reflvy.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

class NonScrollableNestedScrollView : NestedScrollView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // Mencegah intersepsi sentuhan (touch interception)
        return false
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        // Memastikan bahwa tindakan sentuhan tidak diabaikan
        return true
    }
}
