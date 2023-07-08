package com.example.taskmanager.json.implementation

import com.example.taskmanager.json.TaskConverter
import com.example.taskmanager.model.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskConverter : TaskConverter {
    override fun serializeTaskList(tasks: List<Task>): String {
        val gson = Gson()
        return gson.toJson(tasks)
    }

    override fun deserializeTaskList(json: String): MutableList<Task> {
        val gson = Gson()
        val listType = object : TypeToken<MutableList<Task>>() {}.type
        return gson.fromJson(json, listType)
    }
}