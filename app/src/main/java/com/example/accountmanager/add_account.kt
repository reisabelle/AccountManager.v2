package com.example.accountmanager

import android.accounts.Account
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.accountmanager.Account_Data
import com.example.accountmanager.R
import com.example.accountmanager.databinding.ActivityAddAccountBinding
import com.example.accountmanager.home
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class add_account : AppCompatActivity() {

    private lateinit var binding: ActivityAddAccountBinding
    private lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAccountBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backbtn: ImageView = findViewById(R.id.backbtn3)
        backbtn.setOnClickListener {
            val intent = Intent(this, home::class.java)
            startActivity(intent)
        }

        binding.submitbtn.setOnClickListener{
            val platform = binding.platform.text.toString().trim()
            val link = binding.link.text.toString().trim()
            val email = binding.email2.text.toString().trim()
            val phone = binding.phoneNo.text.toString().trim()
            val password = binding.accPassword.text.toString().trim()

            database = FirebaseDatabase.getInstance().reference
            if (platform.isEmpty() || link.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                val account = hashMapOf(
                    "platform" to platform,
                    "link" to link,
                    "email" to email,
                    "phoneNo" to phone,
                    "password" to password
                )
            database.child("Accounts").push().setValue(account)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, Accounts::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to Save Data", Toast.LENGTH_SHORT).show()
                }

        }
    }
}
