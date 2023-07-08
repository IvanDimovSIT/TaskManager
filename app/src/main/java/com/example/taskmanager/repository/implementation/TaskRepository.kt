package com.example.taskmanager.repository.implementation

import android.content.Context
import android.content.SharedPreferences
import com.example.taskmanager.json.TaskConverter
import com.example.taskmanager.model.Task
import com.example.taskmanager.repository.TaskRepository
import java.util.ArrayList

class TaskRepository(private val context: Context) : TaskRepository{
    private var taskList: MutableList<Task>
    private var idCounter: Int = 0
    private val sharedPreferences: SharedPreferences
    private val MAX_ID_KEY: String = "max_id"

    init {
        sharedPreferences = context.getSharedPreferences("TaskRepository", Context.MODE_PRIVATE)
        taskList = ArrayList<Task>()
        load()
    }

    override fun getAll() : MutableList<Task>{
        return taskList
    }

    override fun load() {
        val serializedTasks = sharedPreferences.getString("tasks", null)

        if (serializedTasks != null) {
            val json: TaskConverter = com.example.taskmanager.json.implementation.TaskConverter()
            taskList.clear()
            val loadedTaskList = json.deserializeTaskList(serializedTasks)
            taskList.addAll(loadedTaskList)
        }
        loadMaxId()
    }

    override fun store() {
        val json: TaskConverter = com.example.taskmanager.json.implementation.TaskConverter()
        val serializedTasks = json.serializeTaskList(taskList)
        val editor = sharedPreferences.edit()
        editor.putString("tasks", serializedTasks)
        editor.apply()
        saveMaxId()
    }

    override fun getId():Int{
        return idCounter++;
    }

    override fun add(task: Task) {
        taskList.add(task)
    }

    override fun removeCompleted() {
        val completed: MutableList<Task> = ArrayList<Task>()
        for (i in taskList){
            if(i.isCompleted)
                completed.add(i)
        }
        taskList.removeAll(completed)
    }

    override fun findById(id: Int):Task?{
        for(i in taskList){
            if(i.id == id)
                return i
        }

        return null
    }

    override fun updateTaskById(id: Int, task: Task){
        val foundTask = findById(id)
        if(foundTask == null)
            return

        foundTask.isCompleted = task.isCompleted
        foundTask.title = task.title
        foundTask.dueDate = task.dueDate
        foundTask.note = task.note
        foundTask.priority = task.priority
    }

    private fun loadMaxId() {
        idCounter = sharedPreferences.getInt(MAX_ID_KEY, 0)
    }

    private fun saveMaxId() {
        val editor = sharedPreferences.edit()
        editor.putInt(MAX_ID_KEY, idCounter)
        editor.apply()
    }

}