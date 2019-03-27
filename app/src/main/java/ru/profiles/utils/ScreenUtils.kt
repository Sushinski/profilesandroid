package ru.profiles.utils

import android.content.Context
import android.graphics.Point
import android.view.WindowManager



class ScreenUtils {

    companion object {
        fun getScreenWidth(context: Context): Int {
            val p = Point()
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(p)
            return p.x
        }

        fun calculateBackdropHeight(width: Int): Int {
            return Math.ceil(width.toDouble() / (16f / 9f)).toInt()
        }
    }
}