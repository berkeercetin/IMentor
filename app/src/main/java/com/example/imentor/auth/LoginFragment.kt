package com.example.imentor.auth

import GlobalService
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.imentor.MainActivity
import com.example.imentor.R
import com.example.imentor.auth.services.concrates.AuthManager
import com.example.imentor.entities.User
import com.example.imentor.interfaces.HideToolbarInterface
import com.example.imentor.main.AddTask
import com.example.imentor.main.HomeFragment
import com.example.imentor.services.concrates.UserManager
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider


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
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        oneTapClient = Identity.getSignInClient(requireActivity())
        configureGoogleSignIn()
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
            signInWithGoogle()
        }
        buttonlogin.setOnClickListener {
            login(emailEditText.text.toString(),passwordEditText.text.toString())
        }
        forgotPassword.setOnClickListener {
            val fragment = ForgotPasswordFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        register.setOnClickListener {
            val fragment = RegisterFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
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
                    val fragment = HomeFragment()
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }

            }
            .addOnFailureListener{
                Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()


            }

    }

    private fun configureGoogleSignIn() {

        val gso = GoogleSignInOptions.Builder()
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }
    private fun signInWithGoogle() {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(requireContext())

        if (resultCode == ConnectionResult.SUCCESS) {
            val signInIntent = googleSignInClient.signInIntent
            signInLauncher.launch(signInIntent)
        } else {
            // Google Play Services kullanılabilirlik sorununu ele al
            googleApiAvailability.showErrorDialogFragment(requireActivity(), resultCode, 0)
        }

       // startActivityForResult(signInIntent, 9001)
    }


    // Override onActivityResult method to handle the sign-in result
    private val signInLauncher = registerForActivityResult (ActivityResultContracts.StartActivityForResult ()) { result ->
        Log.d("TAG", "onActivityResult: result code - ${result.resultCode}")
        Log.d("TAG", "onActivityResult: data - ${result.data?.extras?.keySet()}")
        if (result.resultCode == Activity.RESULT_OK) {
            // Giriş işlemini işle
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                authService.firebaseAuthWithGoogle(account.idToken!!).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val authUser = auth.currentUser
                        val user = User(authUser?.uid.toString(),authUser?.displayName.toString(),authUser?.email.toString())
                        userService.registerFirestore(user).addOnSuccessListener {
                            val mainActivity = activity as MainActivity?
                            mainActivity?.setNavHeader(user)
                            val fragment = HomeFragment()
                            val transaction = requireActivity().supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.fragmentContainerView, fragment)
                            transaction.addToBackStack(null)
                            transaction.commit()
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithCredential:failure", it.exception)
                    }
                }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
            }
        }else {
            Log.w("A", "onActivityResult: result code is not OK")
        }
    }



}