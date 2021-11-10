package com.example.todo.database

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryAndToDos(
    @Embedded
    val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val toDos: List<ToDo>
)
