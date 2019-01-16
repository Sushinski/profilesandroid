package ru.profiles.extensions

import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.fragment.app.Fragment
import ru.profiles.profiles.R

fun Fragment.ensureFields(fields: Array<EditText>, reaction: (et: EditText)->Unit) : Boolean {
    fields.firstOrNull{it.text.isBlank()}
        .also { if(it != null ) reaction(it) }
        .let{ return it == null }
}

fun Fragment.shakeField(view: EditText){
    view.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.shaking))
}