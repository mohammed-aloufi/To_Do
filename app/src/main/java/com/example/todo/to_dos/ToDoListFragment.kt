package com.example.todo.to_dos

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.todo.R
import com.example.todo.ToDoDetailsFragment
import com.example.todo.cateogry.CategoryPickerFragment
import com.example.todo.database.Category
import com.example.todo.cateogry.CategoryViewModel
import com.example.todo.cateogry.CategoryBottomSheetFragment
import com.example.todo.database.ToDo
import com.example.todo.new_to_do.NEW_CATEGORY_KEY
import com.example.todo.new_to_do.NewToDoFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

const val EXTRA_ID = "id"
const val EDIT_CATEGORY_TAG = "edit"

class ToDoListFragment : Fragment(), CategoryPickerFragment.CategoryPickerCallBack {

    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var toDoListRecyclerView: RecyclerView
    private lateinit var newToDoActionButton: FloatingActionButton
    private lateinit var emptyImageView: ImageView
    private lateinit var noToDoTextView: TextView

    private val toDoViewModel by lazy {
        ViewModelProvider(this).get(ToDoViewModel::class.java)
    }
    private val categoryViewModel by lazy {
        ViewModelProvider(this).get(CategoryViewModel::class.java)
    }
//    private val category = Category()

    private var categoriesColors: MutableMap<UUID, Int> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_to_do_list, container, false)

        setHasOptionsMenu(true)
        initViews(view)
        setLayoutManger()
        (categoriesRecyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllToDos()

        categoryViewModel.liveDataCategory.observe(
            viewLifecycleOwner, Observer {
                updateCategoryUI(it)
            })
    }

    override fun onStart() {
        super.onStart()
        newToDoBtnSetClickListener()
    }

    /** Action Menu */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_to_do_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sortMenuButton -> {
                Toast.makeText(context, "Sort pressed", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.editCategoryMenuBtn -> {
                handleEditCategoryButtonPressed()
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

    private fun initViews(view: View) {
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView)
        toDoListRecyclerView = view.findViewById(R.id.toDoListRecyclerView)
        newToDoActionButton = view.findViewById(R.id.newToDoActionButton)
        emptyImageView = view.findViewById(R.id.emptyImageView)
        noToDoTextView = view.findViewById(R.id.noToDoTextView)
    }

    private fun setLayoutManger() {
        val toDoLayoutManager = LinearLayoutManager(context)
        val categoryLayoutManger =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        categoriesRecyclerView.layoutManager = categoryLayoutManger
        toDoListRecyclerView.layoutManager = toDoLayoutManager
    }

    private fun getAllToDos(){
        toDoViewModel.liveDataToDo.observe(
            viewLifecycleOwner, Observer {
                updateToDoUI(it)
            })
    }
    private fun getAllByCategory(id: UUID){
        toDoViewModel.getAllByCategoryLiveData(id).observe(
            viewLifecycleOwner, Observer {
                updateToDoUI(it)
            })
    }

    private fun newToDoBtnSetClickListener() {
        newToDoActionButton.setOnClickListener {
            val newToDoFragment = NewToDoFragment()
            newToDoFragment.show(parentFragmentManager, "new-toDo")
        }
    }

    private fun handleEditCategoryButtonPressed(){
        val categoryPicker = CategoryPickerFragment()
        categoryPicker.setTargetFragment(this, 0)
        categoryPicker.show(parentFragmentManager, EDIT_CATEGORY_TAG)
    }

    override fun onCategorySelected(category: Category) {
        handleEditCategoryButtonPressed()
    }

    /** UI updates */
    private fun updateToDoUI(toDos: List<ToDo>) {
        if (toDos.isEmpty()) {
            toDoListRecyclerView.visibility = View.GONE
            emptyImageView.visibility = View.VISIBLE
            noToDoTextView.visibility = View.VISIBLE
        } else {
            emptyImageView.visibility = View.GONE
            noToDoTextView.visibility = View.GONE
            toDoListRecyclerView.visibility = View.VISIBLE
            val toDoAdapter = ToDoAdapter(toDos)
            toDoListRecyclerView.adapter = toDoAdapter
        }
    }

    private fun updateCategoryUI(categories: List<Category>) {
        //creates category 'All' at first run
        if (categories.count() == 0){
            val category = Category()
            categoryViewModel.addCategory(category)
        }
        val categoryAdapter = CategoryAdapter(categories)
        categoriesRecyclerView.adapter = categoryAdapter

        categories.forEach {
            categoriesColors[it.id] = it.color
        }
    }

    //TODO: !!!!BUG!!!! when user go to details and delete or create new category while specific category selected it shows All's to dos in the selected category
    //TODO: do more testing on that
    /** toDoRecyclerView's ViewHolder & Adapter */
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
            itemView.setOnClickListener(this)
            rescheduleToDoBtn.visibility = View.GONE
        }

        fun bind(toDo: ToDo) {
            this.toDo = toDo
            //TODO: change toDo.isDone in database when toDoIsDoneCheckBox.isChecked (true/false)
            toDoIsDoneCheckBox.isChecked = toDo.isDone
            toDoTitleTv.text = toDo.title

            when (toDo.dueDate) {
                null -> toDoDueDateTv.visibility = View.GONE
                else -> {
                    val dateString = android.text.format.DateFormat.format(dateFormat, toDo.dueDate)
                    toDoDueDateTv.text = dateString
                }
            }

            categoriesColors[toDo?.categoryId]?.let {
                toDoCategoryView.setBackgroundColor(resources.getColor(it))
            }
        }

        override fun onClick(v: View?) {
            when (v) {
                itemView -> {
                    val args = Bundle()
                    args.putSerializable(
                        EXTRA_ID,
                        toDo.id
                    )
                    val detailsFragment = ToDoDetailsFragment()
                    detailsFragment.arguments = args

                    activity?.let {
                        it.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.fragmentContainerView, detailsFragment)
                            .addToBackStack("")
                            .commit()
                    }
                }
            }
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

    var selectedItemPos = 0
    var lastItemSelectedPos = 0

    /** CategoriesRecyclerView's ViewHolder & Adapter */
    private inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val categoryButton: Button? = view.findViewById(R.id.categoryButton)
        private val addCategoryImageButton: ImageButton? =
            view.findViewById(R.id.addCategoryImageButton)
        private val selectedView: View? = view.findViewById(R.id.selectedView)

        private lateinit var category: Category

        init {
            categoryButton?.setOnClickListener {
                selectedItemPos = adapterPosition
                if (lastItemSelectedPos == -1)
                    lastItemSelectedPos = selectedItemPos
                else {
                    categoriesRecyclerView.adapter?.notifyItemChanged(lastItemSelectedPos)
                    lastItemSelectedPos = selectedItemPos
                }
                categoriesRecyclerView.adapter?.notifyItemChanged(selectedItemPos)
                if (selectedItemPos == 0){
                    getAllToDos()
                }else {
                    getAllByCategory(category.id)
                }
            }
        }

        fun bindCategory(category: Category) {
            this.category = category
            categoryButton?.text = category.name
            categoryButton?.setBackgroundColor(resources.getColor(category.color))
        }

        fun bindAddButton() {
            addCategoryImageButton?.setOnClickListener {
                val newCategory = CategoryBottomSheetFragment()
                newCategory.show(parentFragmentManager, NEW_CATEGORY_KEY)
            }
        }

        fun defaultBg() {
            selectedView?.setBackgroundColor(resources.getColor(R.color.white))
        }

        fun selectedBg() {
            selectedView?.setBackgroundColor(resources.getColor(R.color.black))
        }
    }

    private inner class CategoryAdapter(var categories: List<Category>) :
        RecyclerView.Adapter<CategoryViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
            return when (viewType) {
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
            when (getItemViewType(position)) {
                R.layout.category_list_item -> {
                    if (position == selectedItemPos) {
                        holder.selectedBg()
//                        selectedCategory(categories[position].id)
                    }
                    else {
                        holder.defaultBg()
                    }
                    val category = categories[position]
                    holder.bindCategory(category)
                }
                R.layout.add_category_button -> {
                    holder.bindAddButton()
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return when (position) {
                categories.size -> R.layout.add_category_button
                else -> R.layout.category_list_item
            }
        }

        override fun getItemCount(): Int = categories.size + 1
    }
}