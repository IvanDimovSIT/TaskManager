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
import android.widget.Toast
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
        val title: String = dialogView.findViewById<EditText>(R.id.editTaskTitle).text.toString().trim()
        if(title.isBlank()){
            showToast(requireContext(), "Въведете заглавие")
            return null
        }

        val note: String = dialogView.findViewById<EditText>(R.id.editNote).text.toString()
        val dateControl: EditText = dialogView.findViewById<EditText>(R.id.editDate)

        val date: Date? = extractDate(dateControl)
        if(date == null){
            showToast(requireContext(), "Въведете валидна дата (година-месец-ден)")
            return null
        }

        val priorityString: String = dialogView.findViewById<Spinner>(R.id.spnPriority).selectedItem.toString()
        var priority: Priority = Priority.LOW
        when(priorityString){
            HIGH_PRIORITY -> priority = Priority.HIGH
            MEDIUM_PRIORITY -> priority = Priority.MEDIUM
            LOW_PRIORITY -> priority = Priority.LOW
        }

        return Task(task.id,title, priority, note, date, task.isCompleted)
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


        builder.setPositiveButton("Запази", null)

        builder.setNegativeButton("Отказ") { dialog, _ ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.setOnShowListener { dialogInterface ->
            val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val task: Task? = readInput(dialogView)
                if (task != null) {
                    observer.onTaskEdited(task)
                    dialog.dismiss()
                }
            }
        }

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        return dialog
    }
}

