package com.example.todo.cateogry

import androidx.lifecycle.ViewModel
import com.example.todo.R
import com.example.todo.cateogry.Category

class CategoryViewModel: ViewModel() {

    val categories = listOf(
        Category(title = "All", R.color.purple_200),
        Category(title = "Work", R.color.teal_200),
        Category(title = "Groceries", R.color.purple_700)
    )

    val colors = listOf(
        R.color.purple_200,
        R.color.teal_200,
        R.color.purple_700
    )
}