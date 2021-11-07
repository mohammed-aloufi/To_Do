package com.example.todo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ToDoListFragment : Fragment() {

    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var toDoListRecyclerView: RecyclerView

    private val toDoViewModel by lazy {
        ViewModelProvider(this).get(ToDoViewModel::class.java)
    }

    private val categoryViewModel by lazy {
        ViewModelProvider(this).get(CategoryViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_to_do_list, container, false)

        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView)
        toDoListRecyclerView = view.findViewById(R.id.toDoListRecyclerView)

        val toDoLayoutManager = LinearLayoutManager(context)
        val categoryLayoutManger = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        categoriesRecyclerView.layoutManager = categoryLayoutManger
        toDoListRecyclerView.layoutManager = toDoLayoutManager

        updateToDoUI()
        updateCategoryUI()
        return view
    }

    private fun updateToDoUI() {
        val toDoAdapter = ToDoAdapter(toDoViewModel.toDos)
        toDoListRecyclerView.adapter = toDoAdapter
    }

    private fun updateCategoryUI(){
        val categoryAdapter = CategoryAdapter(categoryViewModel.categories)
        categoriesRecyclerView.adapter = categoryAdapter
    }

    private inner class ToDoViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private val toDoIsDoneCheckBox: CheckBox = view.findViewById(R.id.isDoneCheckBox)
        private val toDoCategoryView: View = view.findViewById(R.id.toDoCategoryView)
        private val toDoTitleTv: TextView = view.findViewById(R.id.toDoTitleTv)
        private val toDoDueDateTv: TextView = view.findViewById(R.id.toDoDueDateTv)
        private val rescheduleToDoBtn: Button = view.findViewById(R.id.rescheduleButton)

        private lateinit var toDo: ToDo
        private val dateFormat = "EEE, MMM dd"
        init {
            rescheduleToDoBtn.visibility = View.GONE
        }

        fun bind(toDo: ToDo) {
            this.toDo = toDo

            toDoIsDoneCheckBox.isChecked = toDo.isDone
            toDoTitleTv.text = toDo.title

            val dateString = android.text.format.DateFormat.format(dateFormat, toDo.date)
            toDoDueDateTv.text = dateString
        }

        override fun onClick(v: View?) {

        }

    }

    private inner class ToDoAdapter(var toDos: List<ToDo>) :
        RecyclerView.Adapter<ToDoViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {

            val view = layoutInflater.inflate(R.layout.to_do_list_item, parent, false)
            return ToDoViewHolder(view)
        }

        override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
            val toDo = toDos[position]
            holder.bind(toDo)
        }

        override fun getItemCount(): Int = toDos.size

    }

    private inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val categoryButton: Button = view.findViewById(R.id.categoryButton)
        private lateinit var category: Category

        fun bind(category: Category){
            this.category = category
            categoryButton.text = category.title
        }

    }

    private inner class CategoryAdapter(var categories: List<Category>)
        : RecyclerView.Adapter<CategoryViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
            val view = layoutInflater.inflate(R.layout.category_list_item, parent, false)

            return CategoryViewHolder(view)
        }

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            val category = categories[position]
            holder.bind(category)
        }

        override fun getItemCount(): Int = categories.size

    }
}