package com.example.imentor.services.concrates

import com.example.imentor.entities.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserManager {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun registerFirestore (user: User):Task<Void>{
            return db.document("/users/"+user.uid.toString()).set(user)
    }


}