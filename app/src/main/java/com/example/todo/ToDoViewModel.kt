package com.example.todo

import androidx.lifecycle.ViewModel

class ToDoViewModel: ViewModel() {

    val toDos = mutableListOf<ToDo>()

    init {
        for (i in 0..15){
            var toDo = ToDo()
            toDo.title = "To Do ${i+1}"
            toDo.isDone = i % 2 == 0
            toDos += toDo
        }
    }
}