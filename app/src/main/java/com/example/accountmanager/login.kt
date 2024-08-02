package com.example.accountmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class login : AppCompatActivity() {
    
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val backBtn1: ImageView = findViewById(R.id.backbtn1)
        backBtn1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val adminLogbtn: Chip = findViewById(R.id.adminLogbtn)
        adminLogbtn.setOnClickListener {
            val intent = Intent(this, adminLog::class.java)
            startActivity(intent)
        }

        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val loginBtn: Button = findViewById(R.id.login)

        loginBtn.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()


            if (email.isEmpty() || password.isEmpty()) {

                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                signInWithEmailAndPassword(email, password)

            }

        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        checkUserStatus(it.uid) { isDisabled ->
                            if (isDisabled) {
                                // User is disabled, sign them out and show a message
                                auth.signOut()
                                Toast.makeText(this, "Your account has been disabled", Toast.LENGTH_SHORT).show()
                            } else {
                                // User is not disabled, proceed to the next activity
                                val intent = Intent(this, home::class.java)
                                // Pass the email & password value to home
                                intent.putExtra("PASSWORD_KEY", password)
                                intent.putExtra("EMAIL_KEY", email)
                                startActivity(intent)
                                finish()
                            }
                        }
                    } ?: run {
                        // Handle the case where user is null
                        Toast.makeText(baseContext, "User not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun checkUserStatus(uid: String, callback: (Boolean) -> Unit) {
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(uid)
        userRef.child("disabled").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val isDisabled = dataSnapshot.getValue(Boolean::class.java) ?: false
                callback(isDisabled)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("CheckUserStatus", "Database error: ${error.message}")
                callback(true) // Default to disabled if there is an error
            }
        })
    }


}
