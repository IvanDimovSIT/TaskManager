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
    DialogFragment() {
    interface AddTaskDialogObserver{
        fun onTaskAdded(task: Task)
        fun getId():Int
    }
    private val LOW_PRIORITY: String = "Нисък Приоритет"
    private val MEDIUM_PRIORITY: String = "Среден Приоритет"
    private val HIGH_PRIORITY: String = "Висок Приоритет"



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
            Task(observer.getId(),title, priority, note, date, false)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Добавяне на нова задача")

        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.task_dialog_layout, null)

        val options = ArrayList<String>()
        options.add(LOW_PRIORITY)
        options.add(MEDIUM_PRIORITY)
        options.add(HIGH_PRIORITY)

        val spinner: Spinner = dialogView.findViewById(R.id.spnPriority)
        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            options
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                return view
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                textView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                return view
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(1)
        spinner.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_grey))

        builder.setView(dialogView)

        builder.setPositiveButton("Потвърди") { dialog, _ ->
            val task: Task? = readInput(dialogView)
            if(task == null){

            }else{
                observer.onTaskAdded(task)
                dialog.dismiss()
            }
        }

        builder.setNegativeButton("Отказ") { dialog, _ ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        return dialog
    }
}
