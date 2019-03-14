package ru.profiles.ui

import android.content.Context
import android.text.method.Touch.onTouchEvent
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


class SwipelessViewPager : ViewPager {

    private var enableSwipe: Boolean = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return enableSwipe && super.onInterceptTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return enableSwipe && super.onTouchEvent(event)

    }
}