package com.example.todo.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo")
    fun getAllToDos(): LiveData<List<ToDo>>

    @Query("SELECT * FROM todo WHERE id=(:id)")
    fun getToDo(id: UUID): LiveData<ToDo?>

    //1- updating
    @Update
    fun updateToDo(toDo: ToDo)

    @Insert
    fun addToDo(toDo: ToDo)

    @Delete
    fun deleteToDo(toDo: ToDo)
}