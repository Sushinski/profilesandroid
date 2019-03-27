package ru.profiles.interfaces

import ru.profiles.model.ServiceWithRelations

interface OnServiceItemClickListener {
    fun onClick(service: ServiceWithRelations)
}