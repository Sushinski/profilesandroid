package ru.profiles.interfaces

import ru.profiles.model.CategoryModel

interface OnCategoryItemClickListener {
    fun onClick(category: CategoryModel)
}