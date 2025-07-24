package com.example.bankdetailsapp.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.R
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.bankdetailsapp.databinding.ActivityMainBinding
import com.example.bankdetailsapp.utils.FirebaseAuthUtil
import com.example.bankdetailsapp.viewmodel.BankViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: BankViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        FirebaseAuthUtil.initAnonymousAuth {
            if (it != null) {
                viewModel.fetchBankDetails()
            }
        }

        binding.btnEdit.setOnClickListener {
            startActivity(
                Intent(this, AddBankActivity::class.java).putExtra("type", "update")
            )
        }

        binding.fabAddBank.setOnClickListener {
            startActivity(
                Intent(this, AddBankActivity::class.java).putExtra("type", "add")
            )
        }

        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Bank Details")
                .setMessage("Are you sure you want to delete your bank details?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.deleteBankDetails { success ->
                        runOnUiThread {
                            if (success) {
                                Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }


        viewModel.bankDetails.observe(this) { details ->
            if (details == null) {
                binding.ivEmptyIllus.visibility = View.VISIBLE
                binding.llBankDetails.visibility = View.GONE
            } else {
                binding.ivEmptyIllus.visibility = View.GONE
                binding.llBankDetails.visibility = View.VISIBLE
                binding.tvName.text = details.name
                binding.tvAccountNumber.text = details.accountNumber
                binding.tvBankName.text = details.bankName
                binding.tvIfscCode.text = details.ifscCode
            }
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.fetchBankDetails()
    }
}