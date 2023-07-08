package com.example.taskmanager.model

import java.io.Serializable
import java.util.Date

data class Task(
    val id: Int,
    var title: String,
    var priority: Priority,
    var note: String,
    var dueDate: Date,
    var isCompleted: Boolean
) : Serializable
