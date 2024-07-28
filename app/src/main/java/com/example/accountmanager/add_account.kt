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
        setContentView(binding.root)

        val originalPassword = intent.getStringExtra("PASSWORD_KEY")
        val originalEmail = intent.getStringExtra("EMAIL_KEY")

        val backbtn: ImageView = findViewById(R.id.backbtn3)
        backbtn.setOnClickListener {
            val intent = Intent(this, Accounts::class.java)
            intent.putExtra("PASSWORD_KEY", originalPassword)
            intent.putExtra("EMAIL_KEY", originalEmail)
            startActivity(intent)
        }

        binding.submitbtn.setOnClickListener{
            val platform = binding.platform.text.toString().trim()
            val link = binding.link.text.toString().trim()
            val email = binding.email2.text.toString().trim()
            val phone = binding.phoneNo.text.toString().trim()
            val password = binding.accPassword.text.toString().trim()
            val userEmail = originalEmail //add this to identify the added accounts by the user

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
                    "password" to password,
                    "userEmail" to userEmail // added this para sa pagdisplay ng data, hindi maisali yung added accounts ng ibang user. pero di ko pa nagagwa
                )
                database.child("Accounts").child(platform).setValue(account)
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
}
