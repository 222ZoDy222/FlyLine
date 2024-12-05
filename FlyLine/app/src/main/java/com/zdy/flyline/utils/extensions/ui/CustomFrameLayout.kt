package com.zdy.flyline.utils.extensions.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

class CustomFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    override fun performClick(): Boolean {
        // Вызов базовой реализации performClick
        super.performClick()
        // Здесь можно добавить дополнительное поведение, если нужно
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {

            performClick()
        }
        return super.onTouchEvent(event)
    }
}
