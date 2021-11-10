package com.example.todo.cateogry

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.database.Category
import com.example.todo.new_to_do.NEW_CATEGORY_KEY
import com.example.todo.to_dos.EDIT_CATEGORY_TAG
import com.example.todo.to_dos.EXTRA_ID
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class CategoryBottomSheetFragment: BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var categoryFragmentLabelTv: TextView
    private lateinit var newCategoryNameTv: EditText
    private lateinit var newCategoryColorRv: RecyclerView
    private lateinit var cancelButton: ImageButton
    private lateinit var saveButton: ImageButton
    private lateinit var deleteButton: ImageButton

    private val categoryViewModel by lazy {
        ViewModelProvider(this).get(CategoryViewModel::class.java)
    }

    private var selectedColor = 0
    private lateinit var category: Category

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.new_category_fragment, container, false)

        initViews(view)
        setViewsBasedOnChoice()
        setLayoutManger()
        updateCategoryColorUI()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCategoryLiveData()
    }
    override fun onStart() {
        super.onStart()
        setClickListeners()
        setTextWatchers()
    }
    override fun onClick(v: View?) {
        when(v){
            cancelButton -> dismiss()
            saveButton -> handleSaveButtonPressed()
            deleteButton -> handleDeleteButtonPressed()
        }
    }

    private fun initViews(view: View){
        categoryFragmentLabelTv = view.findViewById(R.id.categoryFragmentLabelTv)
        newCategoryNameTv = view.findViewById(R.id.newCategoryNameTv)
        newCategoryColorRv = view.findViewById(R.id.newCategoryColorRv)
        cancelButton = view.findViewById(R.id.cancelNewCategoryButton)
        saveButton = view.findViewById(R.id.saveNewCategoryImageBtn)
        deleteButton = view.findViewById(R.id.deleteCategoryImageButton)
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

    private fun getCategoryFromArguments(){
        category= Category()
        val categoryId = arguments?.getSerializable(EXTRA_ID) as UUID
        categoryViewModel.loadCategory(categoryId)
    }

    private fun observeCategoryLiveData(){
        categoryViewModel.categoryLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                category = it
                newCategoryNameTv.setText(it.name)
            }
        })
    }

    private fun handleSaveButtonPressed(){
        when(this.tag){
            EDIT_CATEGORY_TAG -> {
                category.color = selectedColor
                categoryViewModel.updateCategory(category)
            }
            NEW_CATEGORY_KEY -> {
                val name = newCategoryNameTv.text.toString()
                if (!name.isBlank()){
                    val color = when(selectedColor){
                        0 -> R.color.white
                        else -> selectedColor
                    }
                    val category = Category(name = name, color = color)
                    categoryViewModel.addCategory(category)
                    setSelectedColor(0)
                    dismiss()
                }else {
                    Toast.makeText(context, "Category name is required", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleDeleteButtonPressed(){
        categoryViewModel.deleteCategory(category)
        dismiss()
    }

    private fun setLayoutManger(){
        val newCategoryColorLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        newCategoryColorRv.layoutManager = newCategoryColorLayoutManager
    }

    private fun updateCategoryColorUI() {
        val newCategoryColorAdapter = CategoryColorAdapter(categoryViewModel.colors)
        newCategoryColorRv.adapter = newCategoryColorAdapter
    }

    private fun setClickListeners(){
        cancelButton.setOnClickListener(this)
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

    fun setSelectedColor(color: Int){
        selectedColor = color
    }

    private inner class CategoryColorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val categoryColorButton: Button = view.findViewById(R.id.categoryColorButton)

        fun bindCategory(color: Int){
            categoryColorButton.setBackgroundColor(resources.getColor(color))
            categoryColorButton.setOnClickListener {
//                Toast.makeText(context, "Color Pressed", Toast.LENGTH_SHORT).show()
                setSelectedColor(color)
            }
        }
    }

    private inner class CategoryColorAdapter(var colors: List<Int>)
        : RecyclerView.Adapter<CategoryColorViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryColorViewHolder {
            val view = layoutInflater.inflate(R.layout.categories_color, parent, false)
            return CategoryColorViewHolder(view)
        }

        override fun onBindViewHolder(holder: CategoryColorViewHolder, position: Int) {
            val color = colors[position]
            holder.bindCategory(color)
        }

        override fun getItemCount(): Int = colors.size
    }
}