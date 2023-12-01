package com.example.a12_pa

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MyApplication : MultiDexApplication() {
    companion object {
        lateinit var auth: FirebaseAuth
        var email: String? = null
        lateinit var db: FirebaseFirestore



        fun checkAuth(): Boolean {
            val currentUser = auth.currentUser
            email = currentUser?.email
            return currentUser?.isEmailVerified ?: false
        }
    }

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
    }
}

