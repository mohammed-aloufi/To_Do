package com.example.todo.cateogry

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.example.todo.R
import com.example.todo.database.Category
import com.example.todo.database.ToDo
import com.example.todo.new_to_do.NEW_CATEGORY_KEY
import com.example.todo.to_dos.EDIT_CATEGORY_TAG
import com.example.todo.to_dos.EXTRA_TO_DO_ID
import com.example.todo.to_dos.ToDoListFragment
import com.example.todo.to_dos.ToDoViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class CategoryBottomSheetFragment: BottomSheetDialogFragment(), View.OnClickListener, ColorPickerFragment.ColorPickerCallBack {

    private lateinit var categoryFragmentLabelTv: TextView
    private lateinit var newCategoryNameTv: EditText
    private lateinit var cancelButton: ImageButton
    private lateinit var chooseColorButton: Button
    private lateinit var saveButton: ImageButton
    private lateinit var deleteButton: ImageButton

    private val toDoViewModel by lazy {
        ViewModelProvider(this).get(ToDoViewModel::class.java)
    }
    private val categoryViewModel by lazy {
        ViewModelProvider(this).get(CategoryViewModel::class.java)
    }

    private var selectedColor = 0
    private lateinit var category: Category
    private var mainCategoryId: UUID? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_edit_category_fragment, container, false)

        initViews(view)
        setViewsBasedOnChoice()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCategoryLiveData()
        getDefaultCategoryId()
    }
    override fun onStart() {
        super.onStart()
        setClickListeners()
        setTextWatchers()
    }
    override fun onClick(v: View?) {
        when(v){
            cancelButton -> dismiss()
            chooseColorButton -> handleColorBtnPressed()
            saveButton -> handleSaveBtnPressed()
            deleteButton -> handleDeleteBtnPressed()
        }
    }

    private fun initViews(view: View){
        categoryFragmentLabelTv = view.findViewById(R.id.categoryFragmentLabelTv)
        newCategoryNameTv = view.findViewById(R.id.newCategoryNameTv)
        cancelButton = view.findViewById(R.id.cancelButton)
        chooseColorButton = view.findViewById(R.id.chooseColorButton)
        saveButton = view.findViewById(R.id.saveNewCategoryImageBtn)
        deleteButton = view.findViewById(R.id.deleteCategoryImageButton)
        chooseColorButton.setBackgroundResource(R.drawable.color_none)
    }

    private fun setViewsBasedOnChoice(){
        when(this.tag){
           EDIT_CATEGORY_TAG -> {
               categoryFragmentLabelTv.setText(R.string.edit_category_label)
               deleteButton.visibility = View.VISIBLE
               getCategoryFromArguments()
           }
            NEW_CATEGORY_KEY -> {
                categoryFragmentLabelTv.setText(R.string.add_new_category_label)
                deleteButton.visibility = View.INVISIBLE
            }
        }
    }

    private fun observeCategoryLiveData(){
        categoryViewModel.categoryLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                category = it
                newCategoryNameTv.setText(it.name)
                chooseColorButton.setBackgroundResource(categoryViewModel.colorMap.getValue(it.color))
            }
        })
    }

    private fun getDefaultCategoryId(){
        categoryViewModel.liveDataDefaultCategory.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    mainCategoryId = it.id
                }
            })
    }

    private fun setClickListeners(){
        cancelButton.setOnClickListener(this)
        chooseColorButton.setOnClickListener(this)
        saveButton.setOnClickListener(this)
        deleteButton.setOnClickListener(this)
    }

    private fun setTextWatchers(){
        if (this.tag == EDIT_CATEGORY_TAG) {
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    //nothing
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    category.name = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                    //nothing
                }
            }
            newCategoryNameTv.addTextChangedListener(textWatcher)
        }else{
            return
        }
    }

    private fun handleSaveBtnPressed(){
        when(this.tag){
            EDIT_CATEGORY_TAG -> {
                category.color = selectedColor
                categoryViewModel.updateCategory(category)
                dismiss()
            }
            NEW_CATEGORY_KEY -> {
                val name = newCategoryNameTv.text.toString()
                if (!name.isBlank()){
                    val color = when(selectedColor){
                        0 -> R.color.category_color_none
                        else -> selectedColor
                    }
                    val category = Category(name = name, color = color)
                    categoryViewModel.addCategory(category)

                    dismiss()
                }else {
                    Toast.makeText(context, "Category name is required", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleDeleteBtnPressed(){
        getCategoryToDos(category.id)
        categoryViewModel.deleteCategory(category)
    }

    private fun handleColorBtnPressed(){
        val colorPickerFragment = ColorPickerFragment()
        colorPickerFragment.setTargetFragment(this, 0)
        colorPickerFragment.show(parentFragmentManager, "new-toDo")
    }
    private fun getCategoryFromArguments(){
        category= Category()
        val categoryId = arguments?.getSerializable(EXTRA_TO_DO_ID) as UUID
        categoryViewModel.loadCategory(categoryId)
    }

    private fun getCategoryToDos(categoryId: UUID){
        toDoViewModel.liveDataToDo.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer { list ->
                changeCategoryToDefault(list.filter { it.categoryId == categoryId })
            })
    }

    private fun changeCategoryToDefault(toDos: List<ToDo>){
        toDos.forEach { toDo ->
            mainCategoryId?.let {
                toDo.categoryId = it
            }
            toDoViewModel.saveUpdates(toDo)
        }
        dismiss()
    }

    override fun onColorSelected(color: Int) {
        val colorDrawable = categoryViewModel.colorMap.getValue(color)
        chooseColorButton.setBackgroundResource(colorDrawable)
        selectedColor = color
    }
}