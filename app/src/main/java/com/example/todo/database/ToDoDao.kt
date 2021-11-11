package com.example.todo.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface ToDoDao {

    //1- To_Do
    @Query("SELECT * FROM todo")
    fun getAllToDos(): LiveData<List<ToDo>>

    @Query("SELECT * FROM todo WHERE id=(:id)")
    fun getToDo(id: UUID): LiveData<ToDo?>

    @Update
    fun updateToDo(toDo: ToDo)

    @Insert
    fun addToDo(toDo: ToDo)

    @Delete
    fun deleteToDo(toDo: ToDo)

    //Category
    @Query("SELECT * FROM category")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM category WHERE id=(:id)")
    fun getCategory(id: UUID): LiveData<Category?>

    @Query("SELECT * FROM category WHERE name=('All')")
    fun getMainCategory(): LiveData<Category?>

    @Insert
    fun addCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Delete
    fun deleteCategory(category: Category)
}