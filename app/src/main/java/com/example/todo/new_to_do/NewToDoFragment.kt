package com.example.todo.new_to_do

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.todo.R
import com.example.todo.cateogry.CategoryPickerFragment
import com.example.todo.cateogry.CategoryViewModel
import com.example.todo.database.Category
import com.example.todo.database.ToDo
import com.example.todo.to_dos.ToDoViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

const val NEW_CATEGORY_KEY = "new"
class NewToDoFragment: BottomSheetDialogFragment(), View.OnClickListener, DatePickerDialogFragment.DatePickerCallBack, CategoryPickerFragment.CategoryPickerCallBack {

    private lateinit var newToDoTileTv: EditText
    private lateinit var newToDoDescriptionTv: EditText
    private lateinit var dateButton: Button
    private lateinit var categoryButton: ImageButton
    private lateinit var cancelButton: ImageButton
    private lateinit var saveButton: ImageButton

    private var dueDate: Date? = null
    private var categoryId: UUID? = null
    private var defaultCategoryId: UUID? = null

    private val toDoViewModel by lazy {
        ViewModelProvider(this).get(ToDoViewModel::class.java)
    }

    private val categoryViewModel by lazy {
        ViewModelProvider(this).get(CategoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.new_to_do_fragment, container, false)
        initViews(view)
        return view
    }

    override fun onStart() {
        super.onStart()
        setClickListeners()
        getDefaultCategoryId()
    }

    override fun onClick(v: View?) {
        when(v){
            dateButton -> handleDateBtnPressed()
            categoryButton -> handleCategoryBtnPressed()
            cancelButton -> dismiss()
            saveButton -> handleSaveBtnPressed()
            else -> Toast.makeText(context, "Unknown error!!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDateSelected(date: Date) {
        dueDate = date
        val dateFormat = "EEE, MMM dd"
        val dateString = android.text.format.DateFormat.format(dateFormat, date)
        dateButton.text = dateString
    }

    override fun onCategorySelected(category: Category) {
        categoryId = category.id
        categoryButton.setBackgroundResource(categoryViewModel.colorMap.getValue(category.color))
    }

    private fun initViews(view: View){
        newToDoTileTv = view.findViewById(R.id.newToDoTitleTv)
        newToDoDescriptionTv = view.findViewById(R.id.newToDoDescriptionTv)
        dateButton = view.findViewById(R.id.dateButton)
        categoryButton = view.findViewById(R.id.categoryImageButton)
        cancelButton = view.findViewById(R.id.cancelNewToDoButton)
        saveButton = view.findViewById(R.id.saveNewToDoImageButton)
    }

    private fun setClickListeners(){
        dateButton.setOnClickListener(this)
        categoryButton.setOnClickListener(this)
        cancelButton.setOnClickListener(this)
        saveButton.setOnClickListener(this)
    }

    private fun getDefaultCategoryId(){
        categoryViewModel.liveDataDefaultCategory.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    defaultCategoryId = it.id
                }
            })
    }

    private fun handleDateBtnPressed(){
        val datePicker = DatePickerDialogFragment()
        datePicker.setTargetFragment(this, 0)
        datePicker.show(parentFragmentManager, "date")
    }

    private fun handleCategoryBtnPressed(){
        val categoryPicker = CategoryPickerFragment()
        categoryPicker.setTargetFragment(this, 0)
        categoryPicker.show(parentFragmentManager, NEW_CATEGORY_KEY)
    }

    private fun handleSaveBtnPressed(){
        val title = newToDoTileTv.text.toString()
        if (title.isNotBlank()) {
            val description = newToDoDescriptionTv.text.toString()
            val dueDate = this.dueDate
            if (categoryId == null){
                categoryId = defaultCategoryId
            }
            categoryId?.let {
                val toDo = ToDo(title = title, description = description, dueDate = dueDate, categoryId = it)
                toDoViewModel.addToDo(toDo)
            }
            dismiss()
        }else {
            Toast.makeText(context, "A title is required", Toast.LENGTH_SHORT).show()
        }

    }
}