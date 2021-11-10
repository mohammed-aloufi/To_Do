package com.example.todo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.todo.cateogry.CategoryPickerFragment
import com.example.todo.cateogry.CategoryViewModel
import com.example.todo.database.ToDo
import com.example.todo.new_to_do.DatePickerDialogFragment
import com.example.todo.to_dos.EXTRA_ID
import com.example.todo.to_dos.ToDoViewModel
import java.util.*

const val DATE_KEY = "due-date"
class ToDoDetailsFragment : Fragment(), DatePickerDialogFragment.DatePickerCallBack, View.OnClickListener {

    private lateinit var toDoTitleTv: EditText
    private lateinit var toDoDescriptionTv: EditText
    private lateinit var toDoCreationDateTv: TextView
    private lateinit var toDoDateButton: Button
    private lateinit var toDoCategoryImageButton: ImageButton
    private lateinit var saveUpdatesButton: Button
    private lateinit var deleteToDoButton: Button


    private val toDoViewModel by lazy {
        ViewModelProvider(this).get(ToDoViewModel::class.java)
    }
    private val categoryViewModel by lazy {
        ViewModelProvider(this).get(CategoryViewModel::class.java)
    }

    private lateinit var toDo: ToDo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getToDoFromArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_to_do_detailes, container, false)
        initViews(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToDoLiveData()
    }

    override fun onStart() {
        super.onStart()
        setClickListeners()
        setTextWatchers()
    }

    override fun onStop() {
        super.onStop()
        toDoViewModel.saveUpdates(toDo)
    }
    private fun initViews(view: View){
        toDoTitleTv = view.findViewById(R.id.toDoDetailTitleTv)
        toDoDescriptionTv = view.findViewById(R.id.toDoDetailDescripTv)
        toDoCreationDateTv = view.findViewById(R.id.toDoCreationDateTv)
        toDoDateButton = view.findViewById(R.id.toDoDetailDateButton)
        toDoCategoryImageButton = view.findViewById(R.id.toDoDetailCategImageBtn)
        saveUpdatesButton = view.findViewById(R.id.saveUpdatesButton)
        deleteToDoButton = view.findViewById(R.id.deleteToDoButton)
    }

    private fun dateFormat(toDo: ToDo): String {
        val dateFormat = "EEE, MMM dd"
        return DateFormat.format(dateFormat, toDo.dueDate).toString()
    }

    private fun setTextWatchers(){
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //nothing
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when(s.hashCode()){
                    toDoTitleTv.text.hashCode() -> toDo.title = s.toString()
                    toDoDescriptionTv.text.hashCode() -> toDo.description = s.toString()
                }
            }
            override fun afterTextChanged(s: Editable?) {
                //nothing
            }
        }
        toDoTitleTv.addTextChangedListener(textWatcher)
        toDoDescriptionTv.addTextChangedListener(textWatcher)
    }

    private fun setClickListeners(){
        toDoDateButton.setOnClickListener(this)
        toDoCategoryImageButton.setOnClickListener(this)
        saveUpdatesButton.setOnClickListener(this)
        deleteToDoButton.setOnClickListener(this)
    }

    override fun onDateSelected(date: Date) {
        toDo.dueDate = date
        val dateFormat = "EEE, MMM dd"
        val dateString = android.text.format.DateFormat.format(dateFormat, date)
        toDoDateButton.text = dateString
    }

    private fun dismiss(){
        activity?.let {
            it.supportFragmentManager.popBackStackImmediate()
        }
    }

    override fun onClick(v: View?) {
        when(v){
            toDoDateButton -> handleDateButtonPressed()
            toDoCategoryImageButton -> handleCategoryButtonPressed()
            saveUpdatesButton -> handleSaveButtonPressed()
            deleteToDoButton -> handleDeleteButtonPressed()
        }
    }

    private fun observeToDoLiveData(){
        toDoViewModel.toDoLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                toDo = it
                toDoTitleTv.setText(it.title)
                toDoDescriptionTv.setText(it.description)
                toDoCreationDateTv.setText(DateFormat.format("yyyy/MM/dd", it.creationDate))
                toDoDateButton.text = when(toDo.dueDate){
                    null -> getString(R.string.no_date)
                    else -> dateFormat(toDo)
                }
            }
        })
    }

    private fun getToDoCategory(categoryId: UUID){
        //TODO: get the category to set the category button color
        toDo.categoryId?.let {
            categoryViewModel.loadCategory(it)
        }
    }

    private fun getToDoFromArguments(){
        toDo = ToDo()
        val toDoId = arguments?.getSerializable(EXTRA_ID) as UUID
        toDoViewModel.loadToDo(toDoId)
    }

    private fun handleDateButtonPressed(){
        val datePicker = DatePickerDialogFragment()
        if (toDo.dueDate != null){
            val args = Bundle()
            args.putSerializable(DATE_KEY, toDo.dueDate)
            datePicker.arguments = args
        }
        datePicker.setTargetFragment(this, 0)
        datePicker.show(parentFragmentManager, "date")
    }

    private fun handleCategoryButtonPressed(){
        //TODO: handle when user change/select category
        val categoryPicker = CategoryPickerFragment()
        categoryPicker.setTargetFragment(this, 0)
        categoryPicker.show(parentFragmentManager, "category")
    }
    
    private fun handleSaveButtonPressed(){
        val title = toDoTitleTv.text.toString()
        if (!title.isBlank()) {
            toDoViewModel.saveUpdates(toDo)
        }
        dismiss()
    }

    private fun handleDeleteButtonPressed(){
        toDoViewModel.delete(toDo)
        dismiss()
    }
}