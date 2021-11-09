package com.example.todo.cateogry

import androidx.lifecycle.ViewModel
import com.example.todo.R
import com.example.todo.database.Category

class CategoryViewModel: ViewModel() {

    val categories = listOf(
        Category(name = "All", color = R.color.purple_200),
        Category(name = "Work", color = R.color.teal_200),
        Category(name = "Groceries", color = R.color.purple_700)
    )

    val colors = listOf(
        R.color.purple_200,
        R.color.teal_200,
        R.color.purple_700
    )
}