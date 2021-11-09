package com.example.todo.cateogry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewCategoryFragment: BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var newCategoryNameTv: EditText
    private lateinit var newCategoryColorRv: RecyclerView
    private lateinit var cancelButton: ImageButton
    private lateinit var saveButton: ImageButton

    private val categoryViewModel by lazy {
        ViewModelProvider(this).get(CategoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.new_category_fragment, container, false)

        newCategoryNameTv = view.findViewById(R.id.newCategoryNameTv)
        newCategoryColorRv = view.findViewById(R.id.newCategoryColorRv)

        val newCategoryColorLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        newCategoryColorRv.layoutManager = newCategoryColorLayoutManager

        updatecategoryColorUI()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancelButton = view.findViewById(R.id.cancelNewCategoryButton)
        saveButton = view.findViewById(R.id.saveNewCategoyImageBtn)

    }

    override fun onStart() {
        super.onStart()
        cancelButton.setOnClickListener(this)
        saveButton.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v){
            cancelButton -> dismiss()
            saveButton -> dismiss()
        }
    }

    private fun updatecategoryColorUI() {
        val newCategoryColorAdapter = CategoryColorAdapter(categoryViewModel.colors)
        newCategoryColorRv.adapter = newCategoryColorAdapter
    }

    private inner class CategoryColorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val categoryColorButton: Button = view.findViewById(R.id.categoryColorButton)

//        private lateinit var colors: Int

        fun bindCategory(color: Int){
//            this.colors = color
            categoryColorButton.setBackgroundColor(resources.getColor(color))
            categoryColorButton.setOnClickListener {
                Toast.makeText(context, "Color Pressed", Toast.LENGTH_SHORT).show()
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

            //holder means the CrimeViewHolder
            holder.bindCategory(color)
        }

        override fun getItemCount(): Int = colors.size

    }
}