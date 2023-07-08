package com.example.taskmanager.json

import com.example.taskmanager.model.Task

interface TaskConverter {
    fun serializeTaskList(tasks: List<Task>) : String
    fun deserializeTaskList(json: String) : MutableList<Task>
}