package com.example.imentor.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation
import com.example.imentor.MainActivity
import com.example.imentor.R
import com.example.imentor.auth.services.concrates.AuthManager
import com.example.imentor.services.concrates.UserManager


class ForgotPasswordFragment : Fragment() {
    val authService = AuthManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as MainActivity
        val toolbar = activity.findViewById<Toolbar>(R.id.my_toolbar)
        toolbar.visibility = View.GONE
        val buttonlogin = view.findViewById<Button>(R.id.buttonResetPassword)
        val emailEditText = view.findViewById<EditText>(R.id.editTextTextEmailAddressResetPassword)
        buttonlogin.setOnClickListener{
            forgotPassword(emailEditText.text.toString())
        }


    }

    private fun forgotPassword(email:String){
        authService.forgotPassword(email).addOnSuccessListener {
            Toast.makeText(requireContext(),"E Posta Gönderildi",Toast.LENGTH_SHORT).show()
            val action = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment()
            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(action)
        }
    }
}