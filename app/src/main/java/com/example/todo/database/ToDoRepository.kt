package com.example.todo.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Room
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "toDo-database"
class ToDoRepository private constructor(context: Context) {

    private val database: ToDoDatabase = Room.databaseBuilder(
        context.applicationContext,
        ToDoDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val toDoDao = database.toDoDao()
    private val executor = Executors.newSingleThreadExecutor()

    //To_Do
    fun getAllToDos(): LiveData<List<ToDo>> = toDoDao.getAllToDos()
    fun getToDo(id: UUID): LiveData<ToDo?> = toDoDao.getToDo(id)
    fun getAllByCategoryId(id: UUID): LiveData<List<ToDo>> = toDoDao.getAllByCategoryId(id)

    fun updateToDo(toDo: ToDo) {
        executor.execute {
            toDoDao.updateToDo(toDo)
        }
    }
    fun addToDo(toDo: ToDo){
        executor.execute {
            toDoDao.addToDo(toDo)
        }
    }
    fun deleteToDo(toDo: ToDo){
        executor.execute{
            toDoDao.deleteToDo(toDo)
        }
    }

    //Category
    fun getAllCategories(): LiveData<List<Category>> = toDoDao.getAllCategories()
    fun getCategory(id: UUID): LiveData<Category?> = toDoDao.getCategory(id)
    fun getMainCategory(): LiveData<Category?> = toDoDao.getMainCategory()

    fun addCategory(category: Category){
        executor.execute {
            toDoDao.addCategory(category)
        }
    }

    fun updateCategory(category: Category){
        executor.execute {
            toDoDao.updateCategory(category)
        }
    }

    fun deleteCategory(category: Category){
        executor.execute {
            toDoDao.deleteCategory(category)
        }
    }

    companion object{
        private var INSTANCE: ToDoRepository? = null

        fun initialize(context: Context){
            if (INSTANCE == null){
                INSTANCE = ToDoRepository(context)
            }
        }

        fun get(): ToDoRepository{
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}