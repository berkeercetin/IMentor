package com.example.imentor.auth

import GlobalService
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.imentor.MainActivity
import com.example.imentor.R
import com.example.imentor.auth.services.concrates.AuthManager
import com.example.imentor.entities.User
import com.example.imentor.interfaces.HideToolbarInterface
import com.example.imentor.services.concrates.UserManager
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.material.navigation.NavigationView
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
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var auth: FirebaseAuth;
    private val authService = AuthManager()
    private val userService = UserManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        oneTapClient = Identity.getSignInClient(requireActivity())
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.default_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .build()

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
        val forgotPassword = view.findViewById<TextView>(R.id.textView8)
        val register = view.findViewById<TextView>(R.id.textView9)
        val google = view.findViewById<Button>(R.id.button2)
        google.setOnClickListener{
            loginGoogle()
        }
        buttonlogin.setOnClickListener {
            login(emailEditText.text.toString(),passwordEditText.text.toString())
        }
        forgotPassword.setOnClickListener {
            val action =  LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment()
            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(action)
        }
        register.setOnClickListener {
            val action =  LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(action)
        }

    }



    private fun login(email:String, password:String){
        authService.login(email,password)
            .addOnSuccessListener {
               GlobalService.userId = it.user?.uid.toString()
                val navView = view?.findViewById<NavigationView>(R.id.nav_view)
                userService.getUser(GlobalService.userId).addOnSuccessListener {
                        result ->
                    GlobalService.user =result.toObject(User::class.java)
                    val mainActivity = activity as MainActivity?
                    mainActivity?.setNavHeader(result.toObject(User::class.java)!!)

                }
                val action =  LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(action)
            }
            .addOnFailureListener{
                Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()


            }

    }


    private fun loginGoogle(){

        authService.signInWithGoogle().addOnSuccessListener {
            Toast.makeText(requireContext(),"Basarılı",Toast.LENGTH_SHORT).show()

        }
    }
}