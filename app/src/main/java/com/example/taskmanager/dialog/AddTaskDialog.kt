package com.example.taskmanager.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
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
import com.example.taskmanager.repository.implementation.TaskRepository
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale

class AddTaskDialog(private val observer: AddTaskDialogObserver) :
    BaseTaskDialog() {
    interface AddTaskDialogObserver{
        fun onTaskAdded(task: Task)
        fun getId():Int
    }



    private fun readInput(dialogView: View) : Task?{
        val title: String = dialogView.findViewById<EditText>(R.id.editTaskTitle).text.toString()
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

        return Task(observer.getId(),title, priority, note, date, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Добавяне на нова задача")

        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.task_dialog_layout, null)

        val spinner: Spinner = dialogView.findViewById(R.id.spnPriority)

        spinner.adapter = createArrayAdapter()
        spinner.setSelection(1)
        spinner.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_grey))

        builder.setView(dialogView)

        builder.setPositiveButton("Потвърди", null)

        builder.setNegativeButton("Отказ") { dialog, _ ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.setOnShowListener { dialogInterface ->
            val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val task: Task? = readInput(dialogView)
                if (task != null) {
                    observer.onTaskAdded(task)
                    dialog.dismiss()
                }
            }
        }

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        return dialog
    }
}
