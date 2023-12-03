package com.example.imentor.auth

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.imentor.MainActivity
import com.example.imentor.R
import com.example.imentor.interfaces.HideToolbarInterface
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment(), HideToolbarInterface {
    // TODO: Rename and change types of parameters

    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as MainActivity
        val toolbar = activity.findViewById<Toolbar>(R.id.my_toolbar)
        toolbar.visibility = View.GONE
        val buttonlogin = view.findViewById<Button>(R.id.buttonlogin)
        val emailEditText = view.findViewById<EditText>(R.id.editTextTextEmailAddressLogin)
        val passwordEditText = view.findViewById<EditText>(R.id.editTextTextPasswordLogin)

        buttonlogin.setOnClickListener {
            Toast.makeText(
                requireContext(),
                emailEditText.text.toString(),
                Toast.LENGTH_SHORT,
            ).show()
            Toast.makeText(
                requireContext(),
                passwordEditText.text.toString(),
                Toast.LENGTH_SHORT,
            ).show()
            auth.signInWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Authentication Success.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val action =
                            LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(action)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            requireContext(),
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                      //  updateUI(null)
                    }
                }
        }

    }
}