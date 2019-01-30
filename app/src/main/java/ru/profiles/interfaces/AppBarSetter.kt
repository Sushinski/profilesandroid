package ru.profiles.interfaces

import android.content.Context

interface AppBarSetter {
    fun getBarTitle(ctx: Context): String?
}