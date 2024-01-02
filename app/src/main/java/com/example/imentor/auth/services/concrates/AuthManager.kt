package com.example.imentor.auth.services.concrates

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.imentor.R
import com.example.imentor.auth.LoginFragmentDirections
import com.example.imentor.auth.services.abstracts.AuthService
import com.example.imentor.entities.User
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class AuthManager {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
     fun login(email: String, password: String): Task<AuthResult> {
      return  auth.signInWithEmailAndPassword(email, password)

    }
     fun signup(email: String, password: String) : Task<AuthResult> {
        return  auth.createUserWithEmailAndPassword(email, password)

    }

    fun forgotPassword(email: String) :Task<Void>{
        return auth.sendPasswordResetEmail(email)
    }

    fun firebaseAuthWithGoogle(idToken: String): Task<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return auth.signInWithCredential(credential)
    }

    fun logout() {
        return auth.signOut()
    }



}