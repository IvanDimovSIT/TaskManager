package com.example.taskmanager.dialog

import androidx.fragment.app.DialogFragment
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.taskmanager.R
//import com.example.taskmanager.dialog.options

abstract class BaseTaskDialog : DialogFragment() {
    protected val LOW_PRIORITY: String = "Нисък Приоритет"
    protected val MEDIUM_PRIORITY: String = "Среден Приоритет"
    protected val HIGH_PRIORITY: String = "Висок Приоритет"

    protected fun createArrayAdapter(): ArrayAdapter<String> {
        val options = ArrayList<String>()
        options.add(LOW_PRIORITY)
        options.add(MEDIUM_PRIORITY)
        options.add(HIGH_PRIORITY)

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

        return adapter
    }
}