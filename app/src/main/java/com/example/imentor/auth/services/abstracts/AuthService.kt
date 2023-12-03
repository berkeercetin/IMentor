package com.example.imentor.auth.services.abstracts

import com.example.imentor.entities.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface AuthService {

    fun login(email: String,password: String): Task<AuthResult>
    fun signup(email: String, password: String) : Task<AuthResult>
}