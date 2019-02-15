package ru.profiles.extensions

import android.view.KeyEvent
import androidx.fragment.app.Fragment

fun Fragment.disableBackButton() {
    this.view?.setOnKeyListener { v, keyCode, event -> keyCode == KeyEvent.KEYCODE_BACK }
}