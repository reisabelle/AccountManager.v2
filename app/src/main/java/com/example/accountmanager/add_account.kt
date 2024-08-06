package com.example.accountmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.accountmanager.databinding.ActivityAddAccountBinding
import com.google.firebase.database.*

class add_account : AppCompatActivity() {

    private lateinit var binding: ActivityAddAccountBinding
    private lateinit var database: DatabaseReference

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

        val platformSpinner: Spinner = findViewById(R.id.platformSpinner)
        val platforms = arrayOf("Facebook", "Gmail", "Instagram", "GitHub", "Tiktok","Twitter")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, platforms)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        platformSpinner.adapter = adapter

        platformSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedPlatform = parent.getItemAtPosition(position).toString()
                binding.platform.setText(selectedPlatform)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        binding.submitbtn.setOnClickListener {
            val platform = binding.platform.text.toString().trim()
            val link = binding.link.text.toString().trim()
            val email = binding.email2.text.toString().trim()
            val phone = binding.phoneNo.text.toString().trim()
            val password = binding.accPassword.text.toString().trim()
            val userEmail = originalEmail

            database = FirebaseDatabase.getInstance().reference

            if (platform.isEmpty() || link.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                // check for duplication
                database.child("Accounts").orderByChild("email").equalTo(email)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var isDuplicate = false
                            for (accountSnapshot in snapshot.children) {
                                val accountPlatform = accountSnapshot.child("platform").getValue(String::class.java)
                                val accountLink = accountSnapshot.child("link").getValue(String::class.java)
                                if (accountPlatform == platform && accountLink == link) {
                                    isDuplicate = true
                                    break
                                }
                            }
                            if (isDuplicate) {
                                Toast.makeText(this@add_account, "Account already exists", Toast.LENGTH_SHORT).show()
                            } else {
                                val account = hashMapOf(
                                    "platform" to platform,
                                    "link" to link,
                                    "email" to email,
                                    "phoneNo" to phone,
                                    "password" to password,
                                    "userEmail" to userEmail
                                )
                                database.child("Accounts").push().setValue(account)
                                    .addOnSuccessListener {
                                        Toast.makeText(this@add_account, "Data Saved!", Toast.LENGTH_SHORT).show()

                                        val intent = Intent(this@add_account, Accounts::class.java)
                                        intent.putExtra("PASSWORD_KEY", originalPassword)
                                        intent.putExtra("EMAIL_KEY", originalEmail)
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this@add_account, "Failed to Save Data", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@add_account, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }
}