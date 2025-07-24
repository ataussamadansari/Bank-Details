package com.example.bankdetailsapp.repository

import com.example.bankdetailsapp.model.BankDetails
import com.example.bankdetailsapp.utils.FirebaseAuthUtil
import com.google.firebase.firestore.FirebaseFirestore

class BankRepository {
    private val db = FirebaseFirestore.getInstance()

    fun saveBankDetails(details: BankDetails, onResult: (Boolean) -> Unit) {
        val uid = FirebaseAuthUtil.getCurrentUserId() ?: return onResult(false)
        db.collection("bankDetails").document(uid).set(details)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getBankDetails(onResult: (BankDetails?) -> Unit) {
        val uid = FirebaseAuthUtil.getCurrentUserId() ?: return onResult(null)
        db.collection("bankDetails").document(uid).get()
            .addOnSuccessListener {
                val details = it.toObject(BankDetails::class.java)
                onResult(details)
            }
            .addOnFailureListener { onResult(null) }
    }

    fun deleteBankDetails(onResult: (Boolean) -> Unit) {
        val uid = FirebaseAuthUtil.getCurrentUserId() ?: return onResult(false)
        db.collection("bankDetails").document(uid)
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

}
