package com.example.todo.cateogry

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todo.R

class ColorPickerFragment : DialogFragment() {

    interface ColorPickerCallBack {
        fun onColorSelected(color: Int)
    }

    private lateinit var colorPickerRv: RecyclerView

    private val categoryViewModel by lazy {
        ViewModelProvider(this).get(CategoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.color_picker_fragment, container, false)
        initViews(view)
        setLayoutManger()
        if (dialog != null && dialog!!.window != null){
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return view
    }

    private fun initViews(view: View) {
        colorPickerRv = view.findViewById(R.id.colorPickerRv)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateCategoryUI()
    }

    private fun setLayoutManger() {
        val categoryLayoutManger =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        colorPickerRv.layoutManager = categoryLayoutManger
    }

    private fun updateCategoryUI() {
        val categoryAdapter = CategoryAdapter(categoryViewModel.colors)
        colorPickerRv.adapter = categoryAdapter
    }

    /** CategoriesRecyclerView's ViewHolder & Adapter */
    private inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        private val categoryColorButton: Button = view.findViewById(R.id.categoryColorButton)

        private var selectedColor = 0

        init {
            categoryColorButton.setOnClickListener(this)
        }

        fun bindCategory(color: Int) {
            val colorDrawable = categoryViewModel.colorMap.getValue(color)
            selectedColor = color
            categoryColorButton.setBackgroundResource(colorDrawable)
        }

        override fun onClick(v: View?) {
            when (v) {
                categoryColorButton -> {
                    targetFragment?.let {
                        (it as ColorPickerCallBack).onColorSelected(selectedColor)
                    }
                    dismiss()
                }
            }
        }
    }

    private inner class CategoryAdapter(var colors: List<Int>) :
        RecyclerView.Adapter<CategoryViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

            val view = layoutInflater.inflate(R.layout.categories_colors_list_item, parent, false)
            return CategoryViewHolder(view)
        }

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            val color = colors[position]
            if (color != R.color.category_color_none){
                holder.bindCategory(color)
            }
        }

        override fun getItemCount(): Int = colors.size - 1
    }
}