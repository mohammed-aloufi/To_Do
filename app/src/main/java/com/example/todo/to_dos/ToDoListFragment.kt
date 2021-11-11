package com.example.todo.to_dos

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.todo.*
import com.example.todo.cateogry.CategoryPickerFragment
import com.example.todo.database.Category
import com.example.todo.cateogry.CategoryViewModel
import com.example.todo.cateogry.CategoryBottomSheetFragment
import com.example.todo.database.ToDo
import com.example.todo.new_to_do.NEW_CATEGORY_KEY
import com.example.todo.new_to_do.NewToDoFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

const val EXTRA_TO_DO_ID = "to do id"
const val EDIT_CATEGORY_TAG = "edit"
const val EXTRA_RESCHEDULE = "reschedule"

class ToDoListFragment : Fragment(), CategoryPickerFragment.CategoryPickerCallBack,
    SortFragment.SortCallBack {

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
    private var categoriesColors: MutableMap<UUID, Int> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_to_do_list, container, false)

        setHasOptionsMenu(true)
        initViews(view)
        setLayoutMangers()
        setAnimations()

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
                sortBtnSetClickListener()
                true
            }
            R.id.editCategoryMenuBtn -> {
                handleEditCategoryBtnPressed()
                true
            }
            R.id.app_bar_search -> {
                //TODO: search feature
                Toast.makeText(context, "Search pressed", Toast.LENGTH_SHORT).show()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCategorySelected(category: Category) {
        handleEditCategoryBtnPressed()
    }

    override fun onSortSelected(sortBy: String) {
        toDoViewModel.sortBy = sortBy
        getAllToDos()
    }

    private fun initViews(view: View) {
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView)
        toDoListRecyclerView = view.findViewById(R.id.toDoListRecyclerView)
        newToDoActionButton = view.findViewById(R.id.newToDoActionButton)
        emptyImageView = view.findViewById(R.id.emptyImageView)
        noToDoTextView = view.findViewById(R.id.noToDoTextView)
    }

    private fun setLayoutMangers() {
        val toDoLayoutManager = LinearLayoutManager(context)
        val categoryLayoutManger =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        categoriesRecyclerView.layoutManager = categoryLayoutManger
        toDoListRecyclerView.layoutManager = toDoLayoutManager
    }

    private fun setAnimations(){
        (categoriesRecyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        (toDoListRecyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    private fun getAllToDos() {
        toDoViewModel.liveDataToDo.observe(
            viewLifecycleOwner, Observer { list ->
                if (toDoViewModel.didSelectCategory) {
                    updateToDoUI(list.filter { it.categoryId == toDoViewModel.selectedCategoryId })
                } else {
                    updateToDoUI(list)
                }
            })
    }

    private fun newToDoBtnSetClickListener() {
        newToDoActionButton.setOnClickListener {
            val newToDoFragment = NewToDoFragment()
            newToDoFragment.show(parentFragmentManager, "new-toDo")
        }
    }

    private fun sortBtnSetClickListener() {
        val sortFragment = SortFragment()
        sortFragment.setTargetFragment(this, 0)
        sortFragment.show(parentFragmentManager, toDoViewModel.sortBy)
    }

    private fun handleEditCategoryBtnPressed() {
        val categoryPicker = CategoryPickerFragment()
        categoryPicker.setTargetFragment(this, 0)
        categoryPicker.show(parentFragmentManager, EDIT_CATEGORY_TAG)
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

            val sortedToDos = sortToDos(toDos)
            val toDoAdapter = ToDoAdapter(sortedToDos)
            toDoListRecyclerView.adapter = toDoAdapter
        }
    }

    private fun sortToDos(toDos: List<ToDo>): List<ToDo>{
        if (toDoViewModel.sortBy == SORT_BY_DUE_DATE){
            return toDos.sortedBy {
                it.dueDate
            }.sortedBy {
                it.isDone
            }
        }

        if (toDoViewModel.sortBy == SORT_BY_CREATION_DATE){
            return toDos.sortedBy {
                it.creationDate
            }.sortedBy {
                it.isDone
            }
        }

        if (toDoViewModel.sortBy == SORT_ALPHABETICALLY){
            return toDos.sortedBy {
                it.title
            }.sortedBy {
                it.isDone
            }
        }

        if (toDoViewModel.sortBy == SORT_BY_CATEGORY){
            return toDos.sortedBy {
                it.categoryId
            }.sortedBy {
                it.isDone
            }
        }else{
            return toDos.sortedBy {
                it.isDone
            }
        }
    }

    private fun updateCategoryUI(categories: List<Category>) {
        //creates default category 'All' at first run
        if (categories.count() == 0) {
            val category = Category()
            categoryViewModel.addCategory(category)
        }
        val categoryAdapter = CategoryAdapter(categories)
        categoriesRecyclerView.adapter = categoryAdapter

        categories.forEach {
            categoriesColors[it.id] = it.color
        }
    }

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
            rescheduleToDoBtn.setOnClickListener(this)
            rescheduleToDoBtn.visibility = View.GONE
        }

        fun bind(toDo: ToDo) {
            this.toDo = toDo
            val today = Date()

            toDoIsDoneCheckBox.isChecked = toDo.isDone
            toDoTitleTv.text = toDo.title
            if (toDo.isDone) {
                handleDoneToDos()
            }

            when (toDo.dueDate) {
                null -> {
                    toDoDueDateTv.visibility = View.GONE
                }
                else -> {
                    val dateString = android.text.format.DateFormat.format(dateFormat, toDo.dueDate)
                    toDoDueDateTv.text = dateString

                    if(today.after(toDo.dueDate)){
                        handleDoneToDos()
                        rescheduleToDoBtn.visibility = View.VISIBLE
                    }else{
                        rescheduleToDoBtn.visibility = View.GONE
                    }

                }
            }

            categoriesColors[toDo.categoryId]?.let {
                toDoCategoryView.setBackgroundColor(resources.getColor(it))
            }

            toDoIsDoneCheckBox.setOnCheckedChangeListener { _, isChecked ->
                toDo.isDone = isChecked
                toDoViewModel.saveUpdates(toDo)
                getAllToDos()
            }
        }

        private fun handleDoneToDos() {
            toDoTitleTv.apply {
                setTypeface(null, Typeface.ITALIC)
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                setTextColor(resources.getColor(R.color.gray))
                text
            }
        }

        override fun onClick(v: View?) {
            when (v) {
                itemView -> {
                    toDoDetailsFragmentShow()
                }
                rescheduleToDoBtn -> {
                    toDoDetailsFragmentShow(EXTRA_RESCHEDULE)
                }
            }
        }

        private fun toDoDetailsFragmentShow(why: String = "edit"){
            val args = Bundle()
            args.putSerializable(
                EXTRA_TO_DO_ID,
                toDo.id
            )
            if (why == EXTRA_RESCHEDULE){
                args.putString(
                    EXTRA_RESCHEDULE,
                    "true"
                )
            }
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
                if (lastItemSelectedPos == -1) {
                    lastItemSelectedPos = selectedItemPos
                } else {
                    categoriesRecyclerView.adapter?.notifyItemChanged(lastItemSelectedPos)
                    lastItemSelectedPos = selectedItemPos
                }
                categoriesRecyclerView.adapter?.notifyItemChanged(selectedItemPos)
                if (selectedItemPos == 0) {
                    toDoViewModel.didSelectCategory = false
                    toDoViewModel.selectedCategoryId = null
                } else {
                    toDoViewModel.didSelectCategory = true
                    toDoViewModel.selectedCategoryId = category.id
                }
                getAllToDos()
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

        fun categoryNotSelected() {
            selectedView?.setBackgroundColor(resources.getColor(R.color.white))
        }

        fun categorySelected() {
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
                        holder.categorySelected()
                    } else {
                        holder.categoryNotSelected()
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