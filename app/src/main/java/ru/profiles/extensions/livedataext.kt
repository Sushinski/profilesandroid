package ru.profiles.extensions

import androidx.lifecycle.LiveData
import ru.profiles.livedata.LiveEvent

fun <T> LiveData<T>.toSingleEvent(): LiveData<T> {
    val result = LiveEvent<T>()
    result.addSource(this) {
        result.value = it
    }
    return result
}