package com.example.todo.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todo.R
import java.util.*

@Entity
data class Category(
    @PrimaryKey var id: UUID = UUID.randomUUID(),
    var name: String = "All",
    var color: Int = R.color.blue
)