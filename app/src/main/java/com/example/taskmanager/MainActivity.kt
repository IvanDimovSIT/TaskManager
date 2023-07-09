package com.example.taskmanager

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.adapter.TaskAdapter
import com.example.taskmanager.dialog.AddTaskDialog
import com.example.taskmanager.model.Task
import com.example.taskmanager.repository.TaskRepository
import com.example.taskmanager.service.TaskService
import com.example.taskmanager.ui.theme.TaskManagerTheme

class MainActivity : AppCompatActivity() {
    private var searchText: String = ""
    private var taskSortType: TaskSortType = TaskSortType.Priority
    private val taskAdapter: TaskAdapter = TaskAdapter(this, object: TaskAdapter.TaskAdapterObserver{
        override fun onUpdated(task: Task) {
            taskRepository.updateTaskById(task.id, task)
            saveRepository()
            updateView()
        }
    })
    private lateinit var taskRepository: TaskRepository
    private val taskService: TaskService =
        com.example.taskmanager.service.implementation.TaskService()

    private fun saveRepository(){
        Thread {
            taskRepository.store()
        }.start()
    }

    private fun sort(tasks: MutableList<Task>):MutableList<Task>{
        when(taskSortType){
            TaskSortType.Priority -> { return taskService.sortByPriority(tasks) }
            TaskSortType.Date -> { return taskService.sortByDueDate(tasks) }
        }
    }

    private fun setAddButtonListener(){
        findViewById<Button>(R.id.btnAdd).setOnClickListener{

            val dialog = AddTaskDialog(object: AddTaskDialog.AddTaskDialogObserver{
                override fun onTaskAdded(task: Task) {
                    taskRepository.add(task)
                    updateView()
                    saveRepository()
                }

                override fun getId(): Int {
                    return taskRepository.getId()
                }
            })
            dialog.show(supportFragmentManager, "Add Task")

        }
    }

    private fun updateView(){
        var tasks = taskService.search(taskRepository.getAll(), searchText)
        tasks = sort(tasks)
        taskAdapter.update(tasks.reversed().toMutableList())
    }

    private fun setSortButtonListener(){
        val button = findViewById<Button>(R.id.btnChangeSort)
        button.setOnClickListener {
            if(taskSortType == TaskSortType.Priority){
                taskSortType = TaskSortType.Date
            }else{
                taskSortType = TaskSortType.Priority
            }
            updateView()
            if(taskSortType == TaskSortType.Date){
                button.setText("Дата")
            }else{
                button.setText("Приоритет")
            }
        }

    }

    private fun setClearButtonListener(){
        findViewById<Button>(R.id.btnClear).setOnClickListener{
            taskRepository.removeCompleted()
            updateView()
            saveRepository()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskRepository =
            com.example.taskmanager.repository.implementation.TaskRepository(applicationContext)
        setContentView(R.layout.main_layout)
        supportActionBar?.hide()
        taskRepository.load()


        val recyclerView = findViewById<RecyclerView>(R.id.rclTasks)
        recyclerView.adapter = taskAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        setAddButtonListener()
        setClearButtonListener()
        setSortButtonListener()
        setSearchListener()

        updateView()
    }

    private fun setSearchListener() {
        val search = findViewById<EditText>(R.id.editSearch)
        search.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchText = search.text.toString()
                updateView()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }
}

