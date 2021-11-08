package com.example.todo.new_to_do

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.todo.R
import com.example.todo.cateogry.Category
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class NewToDoFragment: BottomSheetDialogFragment(), View.OnClickListener, DatePickerDialogFragment.DatePickerCallBack, CategoryPickerFragment.CategoryPickerCallBack {

    private lateinit var newToDoTileTv: EditText
    private lateinit var newToDoDescriptionTv: EditText
    private lateinit var dateButton: Button
    private lateinit var categoryButton: ImageButton
    private lateinit var cancelButton: ImageButton
    private lateinit var saveButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.new_to_do_fragment, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newToDoTileTv = view.findViewById(R.id.newToDoTitleTv)
        newToDoDescriptionTv = view.findViewById(R.id.newToDoDescriptionTv)
        dateButton = view.findViewById(R.id.dateButton)
        categoryButton = view.findViewById(R.id.categoryImageButton)
        cancelButton = view.findViewById(R.id.cancelButton)
        saveButton = view.findViewById(R.id.saveImageButton)
    }

    override fun onStart() {
        super.onStart()
        dateButton.setOnClickListener(this)
        categoryButton.setOnClickListener(this)
        cancelButton.setOnClickListener(this)
        saveButton.setOnClickListener(this)

        dateButton.setOnClickListener{
            val datePicker = DatePickerDialogFragment()

            datePicker.setTargetFragment(this, 0)
            datePicker.show(parentFragmentManager, "date")
        }
    }

    override fun onClick(v: View?) {
        when(v){
            dateButton -> "Date Pressed"
            categoryButton -> {

                val categoryPicker = CategoryPickerFragment()
                categoryPicker.setTargetFragment(this, 0)

                categoryPicker.show(parentFragmentManager, "category")
            }
            cancelButton -> dismiss()
            saveButton -> dismiss() //for now
            else -> "Unknow error!!"
        }
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDateSelected(date: Date) {
        val dateFormat = "EEE, MMM dd"
        val dateString = android.text.format.DateFormat.format(dateFormat, date)
        dateButton.text = dateString
    }

    override fun onCategorySelected(category: Category) {
        val categoryColor = category.color
        categoryButton.setBackgroundColor(resources.getColor(categoryColor))
    }


}