package ru.profiles.extensions

import android.view.View
import android.widget.ListView


fun ListView.getViewByPosition(pos: Int): View {
    val firstListItemPosition = this.firstVisiblePosition
    val lastListItemPosition = firstListItemPosition + this.childCount - 1

    return if (pos < firstListItemPosition || pos > lastListItemPosition) {
        this.adapter.getView(pos, null, this)
    } else {
        val childIndex = pos - firstListItemPosition
        this.getChildAt(childIndex)
    }
}