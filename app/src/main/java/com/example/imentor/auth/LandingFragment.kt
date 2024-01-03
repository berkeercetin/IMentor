package com.example.imentor.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.imentor.MainActivity
import com.example.imentor.R


class LandingFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val activity = requireActivity() as MainActivity
        val toolbar = activity.findViewById<Toolbar>(R.id.my_toolbar)
        toolbar.visibility = View.GONE
        return inflater.inflate(R.layout.fragment_landing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val login = view.findViewById<Button>(R.id.login)
        val register = view.findViewById<Button>(R.id.register)
        login.setOnClickListener {
            val action = LandingFragmentDirections.actionLandingFragmentToLoginFragment()
            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(action)
        }
        register.setOnClickListener {
            val action =
               LandingFragmentDirections.actionLandingFragmentToRegisterFragment()
            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(action)
        }
    }
}