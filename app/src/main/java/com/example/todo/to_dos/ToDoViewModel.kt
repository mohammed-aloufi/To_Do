package com.example.todo.to_dos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.todo.database.ToDo
import com.example.todo.database.ToDoRepository
import java.util.*

class ToDoViewModel: ViewModel() {

    private val toDoRepository = ToDoRepository.get()
    private val toDoIdLiveData = MutableLiveData<UUID>()
    val liveDataToDo = toDoRepository.getAllToDos()

    var toDoLiveData: LiveData<ToDo?> =
        Transformations.switchMap(toDoIdLiveData){
            toDoRepository.getToDo(it)
        }

    fun getAllByCategoryLiveData(id: UUID): LiveData<List<ToDo>>{
        return toDoRepository.getAllByCategoryId(id)
    }

    fun loadToDo(toDoId: UUID){
        toDoIdLiveData.value = toDoId
    }

    fun saveUpdates(toDo: ToDo){
        toDoRepository.updateToDo(toDo)
    }

    fun delete(toDo: ToDo){
        toDoRepository.deleteToDo(toDo)
    }

    fun addToDo(toDo: ToDo){
        toDoRepository.addToDo(toDo)
    }
}