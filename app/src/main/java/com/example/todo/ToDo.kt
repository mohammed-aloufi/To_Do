package com.example.todo

import java.util.*

data class ToDo(
    var id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var isDone: Boolean = false
)