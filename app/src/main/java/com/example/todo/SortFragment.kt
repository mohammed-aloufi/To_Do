package com.example.todo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProvider
import com.example.todo.to_dos.ToDoViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

const val AUTO_SORT = "auto"
const val SORT_BY_DUE_DATE = "due-date"
const val SORT_BY_CREATION_DATE = "creation-date"
const val SORT_ALPHABETICALLY = "alphabetical"
const val SORT_BY_CATEGORY = "category"

class SortFragment: BottomSheetDialogFragment(), View.OnClickListener {

    interface SortCallBack{
        fun onSortSelected(sortBy: String)
    }

    private lateinit var cancelButton: ImageButton
    private lateinit var autoSortRadioButton: RadioButton
    private lateinit var byDueDateRadioButton: RadioButton
    private lateinit var byCreationDateRadioButton: RadioButton
    private lateinit var byAlphabeticalRadioButton: RadioButton
    private lateinit var byCategoryRadioButton: RadioButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sort_fragment, container, false)

        initViews(view)
        setChosenRadio(this.tag)
        return view
    }

    override fun onStart() {
        super.onStart()
        setClickListeners()
    }

    private fun initViews(view: View){
        cancelButton = view.findViewById(R.id.cancelSortButton)
        autoSortRadioButton = view.findViewById(R.id.autoSortRadioButton)
        byDueDateRadioButton = view.findViewById(R.id.byDueDateRadioButton)
        byCreationDateRadioButton = view.findViewById(R.id.byCreationDateRadioButton)
        byAlphabeticalRadioButton = view.findViewById(R.id.byAlphabeticalRadioButton)
        byCategoryRadioButton = view.findViewById(R.id.byCategoryRadioButton)
    }

    private fun setChosenRadio(chosenRadio: String?){
        when(chosenRadio){
            AUTO_SORT -> autoSortRadioButton.isChecked = true
            SORT_BY_DUE_DATE -> byDueDateRadioButton.isChecked = true
            SORT_BY_CREATION_DATE -> byCreationDateRadioButton.isChecked = true
            SORT_ALPHABETICALLY -> byAlphabeticalRadioButton.isChecked = true
            "category" -> byCategoryRadioButton.isChecked = true
        }
    }

    private fun setClickListeners(){
        cancelButton.setOnClickListener(this)
        autoSortRadioButton.setOnClickListener(this)
        byDueDateRadioButton.setOnClickListener(this)
        byCreationDateRadioButton.setOnClickListener(this)
        byAlphabeticalRadioButton.setOnClickListener(this)
        byCategoryRadioButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            cancelButton -> dismiss()
            autoSortRadioButton -> {
                targetFragment?.let {
                    (it as SortCallBack).onSortSelected(AUTO_SORT)
                }
                dismiss()
            }
            byDueDateRadioButton -> {
                targetFragment?.let {
                    (it as SortCallBack).onSortSelected(SORT_BY_DUE_DATE )
                }
                dismiss()
            }
            byCreationDateRadioButton -> {
                targetFragment?.let {
                    (it as SortCallBack).onSortSelected(SORT_BY_CREATION_DATE)
                }
                dismiss()
            }
            byAlphabeticalRadioButton -> {
                targetFragment?.let {
                    (it as SortCallBack).onSortSelected(SORT_ALPHABETICALLY)
                }
                dismiss()
            }
            byCategoryRadioButton -> {
                targetFragment?.let {
                    (it as SortCallBack).onSortSelected(SORT_BY_CATEGORY)
                }
                dismiss()
            }
        }
    }
}