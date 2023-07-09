package com.example.taskmanager.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.taskmanager.R
import com.example.taskmanager.model.Priority
import com.example.taskmanager.model.Task
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EditTaskDialog(private val task: Task, private val observer: EditTaskDialogObserver) : BaseTaskDialog() {
    interface EditTaskDialogObserver{
        fun onTaskEdited(task: Task)
    }


    private fun readInput(dialogView: View) : Task?{
        val title: String = dialogView.findViewById<EditText>(R.id.editTaskTitle).text.toString()
        val note: String = dialogView.findViewById<EditText>(R.id.editNote).text.toString()
        val dateString: String = dialogView.findViewById<EditText>(R.id.editDate).text.toString()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date: Date? = dateFormat.parse(dateString)
        val priorityString: String = dialogView.findViewById<Spinner>(R.id.spnPriority).selectedItem.toString()
        var priority: Priority = Priority.LOW
        when(priorityString){
            HIGH_PRIORITY -> priority = Priority.HIGH
            MEDIUM_PRIORITY -> priority = Priority.MEDIUM
            LOW_PRIORITY -> priority = Priority.LOW
        }

        return if(title.length < 1 || date == null){
            null
        }else{
            Task(task.id,title, priority, note, date, task.isCompleted)
        }
    }

    private fun initTaskControls(dialogView: View){
        val calendar = Calendar.getInstance()
        calendar.time = task.dueDate

        dialogView.findViewById<EditText>(R.id.editTaskTitle).setText(task.title)
        dialogView.findViewById<EditText>(R.id.editNote).setText(task.note)
        dialogView.findViewById<EditText>(R.id.editDate).setText(calendar.get(Calendar.YEAR).toString() +
                "-"+(calendar.get(Calendar.MONTH) + 1).toString()+"-"+
                (calendar.get(Calendar.DAY_OF_MONTH)).toString())
        val spinner =  dialogView.findViewById<Spinner>(R.id.spnPriority)
        when(task.priority){
            Priority.LOW -> {spinner.setSelection(0)}
            Priority.MEDIUM -> {spinner.setSelection(1)}
            Priority.HIGH -> {spinner.setSelection(2)}
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Редактиране")

        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.task_dialog_layout, null)

        val spinner: Spinner = dialogView.findViewById(R.id.spnPriority)

        spinner.adapter = createArrayAdapter()
        spinner.setSelection(0)
        spinner.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_grey))

        initTaskControls(dialogView)
        builder.setView(dialogView)


        builder.setPositiveButton("Запази") { dialog, _ ->
            val task: Task? = readInput(dialogView)
            if (task != null) {
                observer.onTaskEdited(task)
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Отказ") { dialog, _ ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        return dialog
    }
}

