package com.example.todo.cateogry

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.database.Category
import com.example.todo.new_to_do.NEW_CATEGORY_KEY
import com.example.todo.to_dos.EDIT_CATEGORY_TAG
import com.example.todo.to_dos.EXTRA_TO_DO_ID

private var choice_tag = ""

class CategoryPickerFragment : DialogFragment() {

    interface CategoryPickerCallBack {
        fun onCategorySelected(category: Category)
    }

    private lateinit var chooseCategoryRv: RecyclerView

    private val categoryViewModel by lazy {
        ViewModelProvider(this).get(CategoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.category_picker_fragment, container, false)
        initViews(view)
        setLayoutManger()
        if (dialog != null && dialog!!.window != null){
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        choice_tag = this.tag!!
        observeLiveDataCategory()

    }

    private fun initViews(view: View) {
        chooseCategoryRv = view.findViewById(R.id.chooseCategoryRv)
    }

    private fun setLayoutManger() {
        val categoryLayoutManger = LinearLayoutManager(context)
        chooseCategoryRv.layoutManager = categoryLayoutManger
    }

    private fun observeLiveDataCategory(){
        categoryViewModel.liveDataCategory.observe(
            viewLifecycleOwner, Observer {
                updateCategoryUI(it)
            })
    }

    private fun updateCategoryUI(observerCategories: List<Category>) {
        if (choice_tag == EDIT_CATEGORY_TAG){
            //the user can't edit/delete the default category
            val categories = mutableListOf<Category>()
            observerCategories.forEach {
                if (it.name.lowercase() == "all"){
                    return@forEach
                }else{
                    categories.add(it)
                }
            }
            val categoryAdapter = CategoryAdapter(categories)
            chooseCategoryRv.adapter = categoryAdapter
        }else {
            val categoryAdapter = CategoryAdapter(observerCategories)
            chooseCategoryRv.adapter = categoryAdapter
        }
    }

    /** CategoriesRecyclerView's ViewHolder & Adapter */
    private inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        private val categoryColorView: View = view.findViewById(R.id.categoryColorView)
        private val categoryTitleTv: TextView = view.findViewById(R.id.categoryTitleTv)

        private lateinit var category: Category

        init {
            itemView.setOnClickListener(this)
        }

        fun bindCategory(category: Category) {
            this.category = category
            categoryViewModel.colorMap[category.color]?.let {
                categoryColorView.setBackgroundResource(it)
            }
            categoryTitleTv.text = category.name
        }

        override fun onClick(v: View?) {
            when (v) {
                itemView -> {
                    targetFragment?.let {
                        when (choice_tag) {
                            NEW_CATEGORY_KEY -> {
                                targetFragment?.let {
                                    (it as CategoryPickerCallBack).onCategorySelected(category)
                                }
                            }
                            EDIT_CATEGORY_TAG -> {
                                val args = Bundle()
                                args.putSerializable(EXTRA_TO_DO_ID, category.id)
                                val newCategory = CategoryBottomSheetFragment()
                                newCategory.arguments = args
                                newCategory.show(parentFragmentManager, EDIT_CATEGORY_TAG)
                            }
                            else -> return
                        }
                    }
                    dismiss()
                }
            }
        }
    }

    private inner class CategoryAdapter(var categories: List<Category>) :
        RecyclerView.Adapter<CategoryViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

            val view = layoutInflater.inflate(R.layout.category_picker_list_item, parent, false)
            return CategoryViewHolder(view)
        }

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            val category = categories[position]
            holder.bindCategory(category)

        }

        override fun getItemCount(): Int = categories.size
    }
}