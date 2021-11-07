package com.example.todo

import androidx.lifecycle.ViewModel

class CategoryViewModel: ViewModel() {

    val categories = listOf(
        Category(title = "All"),
        Category(title = "Work"),
        Category(title = "Groceries"),
        Category(title = "+")
    )
}