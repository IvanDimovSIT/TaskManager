package com.example.taskmanager.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.taskmanager.R
import com.example.taskmanager.dialog.EditTaskDialog
import com.example.taskmanager.model.Priority
import com.example.taskmanager.model.Task
import java.text.DateFormat
import java.util.ArrayList
import java.util.Date

class TaskAdapter: RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
    interface TaskAdapterObserver{
        fun onUpdated(task: Task)
    }

    private var displayedTasks: MutableList<Task> = ArrayList<Task>()
    private val context: Context
    private val observer: TaskAdapterObserver

    constructor(context: Context, observer: TaskAdapterObserver):super(){
        this.context = context
        this.observer = observer
    }

    private fun formatDate(date: Date): String {
        val dateFormat: DateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
        return dateFormat.format(date)
    }

    fun update(tasks: MutableList<Task>){
        displayedTasks = tasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val viewHolder = TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false)
        )
        viewHolder.itemView.findViewById<CheckBox>(R.id.chkTaskDone).setOnCheckedChangeListener { compoundButton, b ->
            val position: Int = viewHolder.absoluteAdapterPosition
            if(position == RecyclerView.NO_POSITION)
                return@setOnCheckedChangeListener

            val task: Task = displayedTasks[position]
            val textView:TextView = viewHolder.itemView.findViewById(R.id.txtTaskTitle)
            if(task.isCompleted){
                textView.setTextColor(ContextCompat.getColor(context, R.color.black))
            }else{
                textView.setTextColor(ContextCompat.getColor(context, R.color.grey))
            }
            textView.invalidate()
            task.isCompleted = !task.isCompleted
            observer.onUpdated(task)
        }
        viewHolder.itemView.setOnClickListener {
            val position: Int = viewHolder.absoluteAdapterPosition
            if(position == RecyclerView.NO_POSITION)
                return@setOnClickListener
            val dialog: EditTaskDialog = EditTaskDialog(displayedTasks[position],
                object : EditTaskDialog.EditTaskDialogObserver {
                    override fun onTaskEdited(task: Task) {
                        observer.onUpdated(task)
                    }
                })
            dialog.show((context as AppCompatActivity).supportFragmentManager, "Редактиране")
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return displayedTasks.size
    }

    override fun onBindViewHolder(holder: TaskAdapter.TaskViewHolder, position: Int) {
        val task = displayedTasks[position]
        val titleBox = holder.itemView.findViewById<TextView>(R.id.txtTaskTitle)
        titleBox.setText(task.title)

        holder.itemView.findViewById<TextView>(R.id.txtDate).setText("Срок:"+formatDate(task.dueDate))

        val priorityText = holder.itemView.findViewById<TextView>(R.id.txtPriority)
        priorityText.setText("Приоритет:"+task.priority.toString())
        when(task.priority){
            Priority.HIGH -> {priorityText.setTextColor(ContextCompat.getColor(context, R.color.red))}
            Priority.MEDIUM -> {priorityText.setTextColor(ContextCompat.getColor(context, R.color.yellow))}
            Priority.LOW -> {priorityText.setTextColor(ContextCompat.getColor(context, R.color.green))}
        }

    }

}