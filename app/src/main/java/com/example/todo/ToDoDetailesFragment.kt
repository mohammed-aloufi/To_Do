package com.example.todo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton


class ToDoDetailesFragment : Fragment() {

    private lateinit var toDoTitleTv: EditText
    private lateinit var toDoDescriptionTv: EditText
    private lateinit var toDoDateButton: Button
    private lateinit var toDoCategoryImageButton: ImageButton
    private lateinit var saveUpdatesButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_to_do_detailes, container, false)
        initViews(view)

        return view
    }

    private fun initViews(view: View){
        toDoTitleTv = view.findViewById(R.id.toDoDetailTitleTv)
        toDoDescriptionTv = view.findViewById(R.id.toDoDetailDescripTv)
        toDoDateButton = view.findViewById(R.id.toDoDetailDateButton)
        toDoCategoryImageButton = view.findViewById(R.id.toDoDetailCategImageBtn)
        saveUpdatesButton = view.findViewById(R.id.saveUpdatesButton)
    }
}