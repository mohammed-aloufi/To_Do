package com.example.todo.cateogry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        choice_tag = this.tag!!
        observeLiveDataCategory()

    }

    override fun onStart() {
        super.onStart()
        setDialogWidth()
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

    private fun setDialogWidth() {
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
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
            when(category.color){
                R.color.category_color_blue -> setCircleColor(R.drawable.color_blue)
                R.color.category_color_orange -> setCircleColor(R.drawable.color_orange)
                R.color.category_color_yellow -> setCircleColor(R.drawable.color_yellow)
                R.color.category_color_red -> setCircleColor(R.drawable.color_red)
                R.color.category_color_pink -> setCircleColor(R.drawable.color_pink)
                R.color.category_color_purple -> setCircleColor(R.drawable.color_purple)
                R.color.category_color_green -> setCircleColor(R.drawable.color_green)
                R.color.category_color_light_blue -> setCircleColor(R.drawable.color_light_blue)
                R.color.category_color_brown -> setCircleColor(R.drawable.color_brown)
                else -> setCircleColor(R.drawable.color_none)
            }
            categoryTitleTv.text = category.name
        }

        fun setCircleColor(resId: Int){
            categoryColorView.setBackgroundResource(resId)
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