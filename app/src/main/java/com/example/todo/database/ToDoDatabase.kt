package com.example.todo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ToDo::class, Category::class], version = 1)
@TypeConverters(ToDoTypeConverters::class)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
}