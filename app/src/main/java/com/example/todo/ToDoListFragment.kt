package com.example.todo

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ToDoListFragment : Fragment() {

    /** Widgets */
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var toDoListRecyclerView: RecyclerView
    private lateinit var newToDoActionButton: FloatingActionButton

    /** Attributes */
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

        setHasOptionsMenu(true)
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView)
        toDoListRecyclerView = view.findViewById(R.id.toDoListRecyclerView)
        newToDoActionButton = view.findViewById(R.id.newToDoActionButton)

        val toDoLayoutManager = LinearLayoutManager(context)
        val categoryLayoutManger = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        categoriesRecyclerView.layoutManager = categoryLayoutManger
        toDoListRecyclerView.layoutManager = toDoLayoutManager

        updateToDoUI()
        updateCategoryUI()
        return view
    }

    override fun onStart() {
        super.onStart()

        newToDoActionButton.setOnClickListener {
            Toast.makeText(context, "New To Do Pressed", Toast.LENGTH_SHORT).show()
        }
    }

    /** Action Menu */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_to_do_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.sortMenuButton -> {
                Toast.makeText(context, "Sort pressed", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.editCategoryMenuBtn -> {
                Toast.makeText(context, "Edit pressed", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.app_bar_search -> {
                Toast.makeText(context, "Search pressed", Toast.LENGTH_SHORT).show()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }



    /** UI updates */
    private fun updateToDoUI() {
        val toDoAdapter = ToDoAdapter(toDoViewModel.toDos)
        toDoListRecyclerView.adapter = toDoAdapter
    }

    private fun updateCategoryUI(){
        val categoryAdapter = CategoryAdapter(categoryViewModel.categories)
        categoriesRecyclerView.adapter = categoryAdapter
    }

    /** toDoRecyclerView's ViewHolder & Adapter */
    private inner class ToDoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

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

    /** CategoriesRecyclerView's ViewHolder & Adapter */
    private inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val categoryButton: Button? = view.findViewById(R.id.categoryButton)
        private val addCategoryImageButton: ImageButton? =  view.findViewById(R.id.addCategoryImageButton)
        private lateinit var category: Category

        fun bindCategory(category: Category){
            this.category = category
            categoryButton?.text = category.title
            categoryButton?.setOnClickListener {
                Toast.makeText(context, "${category.title} Pressed", Toast.LENGTH_SHORT).show()
            }

        }

        fun bindAddButton() {
            addCategoryImageButton?.setOnClickListener {
                Toast.makeText(context, "Add Category Pressed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private inner class CategoryAdapter(var categories: List<Category>)
        : RecyclerView.Adapter<CategoryViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
            return when(viewType){
                R.layout.category_list_item -> {
                    val view = layoutInflater.inflate(R.layout.category_list_item, parent, false)
                    CategoryViewHolder(view)
                }
                R.layout.add_category_button -> {
                    val view = layoutInflater.inflate(R.layout.add_category_button, parent, false)
                    CategoryViewHolder(view)
                }
                else -> throw IllegalArgumentException("unknown view type $viewType")
            }
        }

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            when(getItemViewType(position)) {
                R.layout.category_list_item -> {
                    val category = categories[position]
                    holder.bindCategory(category)
                }
                R.layout.add_category_button -> {
                    holder.bindAddButton()
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return when(position) {
                categories.size -> R.layout.add_category_button
                else -> R.layout.category_list_item
            }
        }
        override fun getItemCount(): Int = categories.size + 1

    }
}