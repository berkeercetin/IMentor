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
import com.example.imentor.MainActivity
import com.example.imentor.R
import com.example.imentor.interfaces.HideToolbarInterface
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore


class RegisterFragment : Fragment(), HideToolbarInterface {

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


        buttonRegister.setOnClickListener {
            auth.createUserWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        val fireUser = hashMapOf(
                            "name" to name.text.toString(),
                            "uid" to user?.uid.toString(),
                            "country" to "USA",
                        )
                        try {
                            Toast.makeText(
                                requireContext(),
                                user?.uid.toString() ?: "Kullanıcı veya UID null",
                                Toast.LENGTH_SHORT
                            ).show()
                            val db = Firebase.firestore
                            val documentReference = db.document("/users/" + user?.uid.toString())
                            documentReference.set(fireUser)
                                .addOnSuccessListener { _ ->
                                    Log.d(TAG, "Belge başarıyla eklendi")
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Belge eklenirken hata oluştu", e)
                                    Toast.makeText(
                                        requireContext(),
                                        "Belge eklenirken hata oluştu: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } catch (e: Exception) {
                            Log.e(TAG, "Firestore işleminde istisna", e)
                            Toast.makeText(
                                requireContext(),
                                "Firestore işleminde istisna: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        Toast.makeText(
                            requireContext(),
                            task.exception.toString(),
                            Toast.LENGTH_SHORT,
                        ).show()
                        // If sign in fails, display a message to the user.
                      //  Log.w(TAG, "createUserWithEmail:failure", task.exception)

                    }
                }

        }
    }
}