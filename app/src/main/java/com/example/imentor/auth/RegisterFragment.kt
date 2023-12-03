package com.example.imentor.auth

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
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
import com.example.imentor.auth.services.abstracts.AuthService
import com.example.imentor.auth.services.concrates.AuthManager
import com.example.imentor.entities.User
import com.example.imentor.interfaces.HideToolbarInterface
import com.example.imentor.services.concrates.UserManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore


class RegisterFragment : Fragment(), HideToolbarInterface {

    private lateinit var auth: FirebaseAuth;
    val userService = UserManager()
    val authService = AuthManager()

    //val firestoreService = AuthManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = requireActivity() as MainActivity
        val toolbar = activity.findViewById<Toolbar>(R.id.my_toolbar)
        toolbar.visibility = View.GONE
        super.onViewCreated(view, savedInstanceState)
        val buttonRegister = view.findViewById<Button>(R.id.buttonRegisterFirestore)
        val emailEditText = view.findViewById<EditText>(R.id.editTextTextEmailAddress)
        val name = view.findViewById<EditText>(R.id.editTextName)
        val passwordEditText = view.findViewById<EditText>(R.id.editTextTextPassword)
        val surname = view.findViewById<EditText>(R.id.surnameRegister)

        buttonRegister.setOnClickListener {
            authService.signup(emailEditText.text.toString(),passwordEditText.text.toString())
                .addOnSuccessListener { it1 ->
                    val user = User(
                        name.text.toString(),
                        it1.user?.uid.toString(),
                        surname.text.toString(),
                        emailEditText.text.toString()
                    )
                    userService.registerFirestore(user).addOnSuccessListener {it3->
                        Toast.makeText(requireContext(),"Kayıt Başarılı",Toast.LENGTH_SHORT).show()
                        val action =  RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(action)
                    }
                        .addOnFailureListener{it2 ->
                            Toast.makeText(requireContext(),it2.message.toString(),Toast.LENGTH_SHORT).show()
                        }
                }
        }
    }
}