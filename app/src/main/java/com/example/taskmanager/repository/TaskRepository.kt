package com.example.taskmanager.repository

import com.example.taskmanager.model.Task

interface TaskRepository {
    fun getAll(): MutableList<Task>
    fun load()
    fun store()
    fun add(task: Task)
    fun removeCompleted()
    fun getId():Int
    fun findById(id: Int):Task?
    fun updateTaskById(id: Int, task: Task)
}