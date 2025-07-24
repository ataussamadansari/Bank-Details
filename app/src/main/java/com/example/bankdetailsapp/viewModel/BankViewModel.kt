package com.example.bankdetailsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bankdetailsapp.model.BankDetails
import com.example.bankdetailsapp.repository.BankRepository

class BankViewModel : ViewModel() {
    private val repository = BankRepository()

    private val _bankDetails = MutableLiveData<BankDetails?>()
    val bankDetails: LiveData<BankDetails?> = _bankDetails

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun fetchBankDetails() {
        _loading.value = true
        repository.getBankDetails {
            _bankDetails.value = it
            _loading.value = false
        }
    }

    fun saveBankDetails(details: BankDetails, onComplete: (Boolean) -> Unit) {
        _loading.value = true
        repository.saveBankDetails(details) {
            if (it) fetchBankDetails()
            _loading.value = false
            onComplete(it)
        }
    }

    fun deleteBankDetails(onComplete: (Boolean) -> Unit) {
        _loading.value = true
        repository.deleteBankDetails { success ->
            if (success) _bankDetails.value = null
            _loading.value = false
            onComplete(success)
        }
    }
}
