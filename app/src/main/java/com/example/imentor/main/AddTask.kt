package com.example.imentor.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.imentor.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddTask.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddTask : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(com.example.imentor.R.layout.fragment_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categorySpinner = view.findViewById<Spinner>(com.example.imentor.R.id.categorySpinner)!!
        val extraInputsLayout = view.findViewById<LinearLayout>(com.example.imentor.R.id.extraInputsLayout)!!
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                val selectedCategory = parentView.getItemAtPosition(position) as String

                // "Sağlık" kategorisi seçildiyse ek inputları görünür yap
                if ("Sağlık" == selectedCategory) {
                    extraInputsLayout.visibility = View.VISIBLE
                } else {
                    extraInputsLayout.visibility = View.GONE
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Bir şey seçilmediğinde yapılacaklar
            }
        }

    }
}