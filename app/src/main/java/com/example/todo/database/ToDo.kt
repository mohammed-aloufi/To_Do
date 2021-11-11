package com.example.todo.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class ToDo(
    @PrimaryKey var id: UUID = UUID.randomUUID(),
    var title: String = "",
    var creationDate: Date? = Date(),
    var dueDate: Date? = Date(),
    var isDone: Boolean = false,
    var description: String = "",
    var categoryId: UUID? = UUID.randomUUID()
)




