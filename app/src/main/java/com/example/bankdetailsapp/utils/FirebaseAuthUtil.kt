package com.example.bankdetailsapp.utils

import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthUtil {
    fun initAnonymousAuth(onComplete: (String?) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            auth.signInAnonymously().addOnCompleteListener {
                if (it.isSuccessful) {
                    onComplete(auth.currentUser?.uid)
                } else {
                    onComplete(null)
                }
            }
        } else {
            onComplete(currentUser.uid)
        }
    }

    fun getCurrentUserId(): String? = FirebaseAuth.getInstance().currentUser?.uid
}