package com.example.todo.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Category(
    @PrimaryKey var id: UUID = UUID.randomUUID(),
    var name: String = "",
    var color: Int? = 1
)