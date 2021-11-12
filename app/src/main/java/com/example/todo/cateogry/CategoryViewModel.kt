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

    //TODO: consider making it private and set a getter
    val colorMap = mapOf(
        R.color.category_color_blue to R.drawable.color_blue,
        R.color.category_color_orange to R.drawable.color_orange,
        R.color.category_color_yellow to R.drawable.color_yellow,
        R.color.category_color_red to R.drawable.color_red,
        R.color.category_color_pink to R.drawable.color_pink,
        R.color.category_color_purple to R.drawable.color_purple,
        R.color.category_color_green to R.drawable.color_green,
        R.color.category_color_light_blue to R.drawable.color_light_blue,
        R.color.category_color_brown to R.drawable.color_brown,
        R.color.category_color_none to R.drawable.color_none,
    )

    val colors = listOf(
        R.color.category_color_blue,
        R.color.category_color_orange,
        R.color.category_color_yellow,
        R.color.category_color_red,
        R.color.category_color_pink,
        R.color.category_color_purple,
        R.color.category_color_green,
        R.color.category_color_light_blue,
        R.color.category_color_brown,
        R.color.category_color_none
    )

    private val toDoRepository = ToDoRepository.get()
    private val categoryIdLiveData = MutableLiveData<UUID>()
    val liveDataCategory = toDoRepository.getAllCategories()
    val liveDataDefaultCategory = toDoRepository.getMainCategory()

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