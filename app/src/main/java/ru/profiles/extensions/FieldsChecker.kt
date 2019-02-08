package ru.profiles.extensions

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.profiles.profiles.R

fun CheckBox.hasChecked(falseReaction: CheckBox.()->Unit, errMessage: String): Boolean{
    return this.isChecked.also {
        if (!it) {
            falseReaction()
            Toast.makeText(this.context, errMessage, Toast.LENGTH_SHORT).show()
        }
    }
}

fun <T : Fragment> T.ensureFields(fields: Array<EditText>, reaction: EditText.()->Unit, errMessage: String) : Boolean {
    fields.firstOrNull{ it.text.isBlank() }
        .also { it?.reaction() }
        .also{ if(it != null) Toast.makeText(this.context, errMessage, Toast.LENGTH_SHORT).show() }
        .let{ return it == null }
}

fun View.shakeField(){
    this.requestFocus()
    this.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.shaking))
}

fun EditText.isEmailValid(falseReaction: EditText.()->Unit, errMessage: String): Boolean{
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this.text).matches()
        .also {
            if (!it) {
                falseReaction()
                Toast.makeText(this.context, errMessage, Toast.LENGTH_SHORT).show()
            }
        }
}

fun EditText.equalTextTo( other: EditText, falseReaction: EditText.()->Unit, errMessage: String) : Boolean {
    return (this.text.trim().toString() == other.text.trim().toString())
        .also {
            if(!it) {
                other.falseReaction()
                Toast.makeText(this.context, errMessage, Toast.LENGTH_SHORT).show()
            }
        }
}