package com.example.taskmanager.service.implementation

import com.example.taskmanager.model.Task
import com.example.taskmanager.service.TaskService
import java.util.ArrayList
import java.util.Comparator

class TaskService : TaskService {

    override fun sortByPriority(tasks: MutableList<Task>): MutableList<Task> {
        val list = ArrayList<Task>(tasks)
        return list.sortedWith(object: Comparator<Task> {
            override fun compare(p0: Task?, p1: Task?): Int {
                if(p0 == null || p1 == null)
                    return 0

                if(p0.priority.getValue() > p1.priority.getValue()){
                    return 1
                }else if(p0.priority.getValue() < p1.priority.getValue()){
                    return -1
                }else{
                    return 0
                }
            }
        }).toMutableList()
    }

    override fun sortByDueDate(tasks: MutableList<Task>):MutableList<Task> {
        val list = ArrayList<Task>(tasks)
        return list.sortedWith(object: Comparator<Task> {
            override fun compare(p0: Task?, p1: Task?): Int {
                if(p0 == null || p1 == null)
                    return 0

                return p0.dueDate.compareTo(p1.dueDate)
            }
        }).toMutableList()
    }

    // от ChatGPT:
    override fun search(tasks: MutableList<Task>, searchText: String): MutableList<Task> {
        if(searchText.isNullOrBlank())
            return tasks

        val searchResults = tasks.filter { task: Task ->
            task.title.contains(searchText, ignoreCase = true)
        }

        return searchResults.toMutableList()
    }
}