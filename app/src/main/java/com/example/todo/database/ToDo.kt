package com.example.todo.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("categoryId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class ToDo(
    @PrimaryKey var id: UUID = UUID.randomUUID(),
    var title: String = "",
    var creationDate: Date? = Date(),
    var dueDate: Date? = Date(),
    var isDone: Boolean = false,
    var description: String = "",
    @ColumnInfo(index = true)
    var categoryId: UUID? = UUID.randomUUID()
)




