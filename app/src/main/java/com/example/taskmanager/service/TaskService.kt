package com.example.taskmanager.service

import com.example.taskmanager.model.Task

interface TaskService {
    fun sortByPriority(tasks: MutableList<Task>) :  MutableList<Task>
    fun sortByDueDate(tasks: MutableList<Task>) :  MutableList<Task>
    fun search(tasks: MutableList<Task>, searchText: String) : MutableList<Task>
}