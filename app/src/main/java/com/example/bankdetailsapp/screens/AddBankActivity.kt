package com.example.bankdetailsapp.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bankdetailsapp.databinding.ActivityAddBankBinding
import com.example.bankdetailsapp.model.BankDetails
import com.example.bankdetailsapp.viewmodel.BankViewModel

class AddBankActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBankBinding
    private val viewModel: BankViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddBankBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbarAddBank.setNavigationOnClickListener {
            finish()
        }

        /*viewModel.loading.observe(this) { isLoading ->
            binding.llLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }*/

        viewModel.loading.observe(this) { isLoading ->
            binding.llLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSave.isEnabled = !isLoading
        }


        val type = intent.getStringExtra("type")
        if (type.equals("add")) {
            binding.toolbarAddBank.title = "Add Bank Details"
            binding.btnSave.text = "Save"

            binding.btnSave.setOnClickListener {
                saveOrUpdateBank()
            }

        } else {
            binding.toolbarAddBank.title = "Update Bank Details"
            binding.btnSave.text = "Update"

            viewModel.fetchBankDetails()
            viewModel.bankDetails.observe(this) { details ->
                if (details != null) {
                    binding.etName.setText(details.name)
                    binding.etAccountNumber.setText(details.accountNumber)
                    binding.etBankName.setText(details.bankName)
                    binding.etIfscCode.setText(details.ifscCode)
                } else {
                    Toast.makeText(this, "Failed to load details", Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnSave.setOnClickListener {
                saveOrUpdateBank()
            }

        }

    }

    private fun saveOrUpdateBank() {
        val name = binding.etName.text.toString().trim()
        val acc = binding.etAccountNumber.text.toString().trim()
        val bank = binding.etBankName.text.toString().trim()
        val ifsc = binding.etIfscCode.text.toString().trim()

        if (name.isNotEmpty() && acc.isNotEmpty() && bank.isNotEmpty() && ifsc.isNotEmpty()) {
            val details = BankDetails(name, acc, bank, ifsc)
            viewModel.saveBankDetails(details) { success ->
                if (success) {
                    Toast.makeText(this, "Bank Details Saved Successfully", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
        }
    }
}