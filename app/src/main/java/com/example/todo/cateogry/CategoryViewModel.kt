package com.example.todo.cateogry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.todo.R
import com.example.todo.database.Category
import com.example.todo.database.ToDo
import com.example.todo.database.ToDoRepository
import java.util.*

class CategoryViewModel: ViewModel() {

    val colors = listOf(
        R.color.purple_200,
        R.color.teal_200,
        R.color.purple_700
    )
    private val toDoRepository = ToDoRepository.get()
    private val categoryIdLiveData = MutableLiveData<UUID>()
    val liveDataCategory = toDoRepository.getAllCategories()
    val liveDataMainCategory = toDoRepository.getMainCategory()

    var categoryLiveData: LiveData<Category?> =
        Transformations.switchMap(categoryIdLiveData){
            toDoRepository.getCategory(it)
        }

    fun loadCategory(categoryId: UUID){
        categoryIdLiveData.value = categoryId
    }

    fun addCategory(category: Category){
        toDoRepository.addCategory(category)
    }

    fun updateCategory(category: Category){
        toDoRepository.updateCategory(category)
    }

    fun deleteCategory(category: Category){
        toDoRepository.deleteCategory(category)
    }
}