package com.example.taskmanager.dialog

import android.content.Context
import androidx.fragment.app.DialogFragment
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.taskmanager.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    protected fun extractDate(editText: EditText) : Date?{
        return try {
            val dateString: String = editText.text.toString()

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateFormat.parse(dateString)
        } catch (e: ParseException) {
            null
        }
    }

    protected fun showToast(context: Context, text: String){
        val toast: Toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast.show()
    }

}