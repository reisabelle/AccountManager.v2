package com.example.accountmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.accountmanager.ModelClassess.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Validation : AppCompatActivity() {

    private lateinit var accountsRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validation)

        Log.d("Validation", "onCreate called")

        auth = FirebaseAuth.getInstance()
        accountsRef = FirebaseDatabase.getInstance().getReference("users")

        val backbtn = findViewById<ImageView>(R.id.backbtn7)
        backbtn.setOnClickListener {
            val intent = Intent(this, Admin::class.java)
            startActivity(intent)
        }

        val delbutton = findViewById<Button>(R.id.deletebtn)
        delbutton.setOnClickListener {
            deleteUser()
        }

        val disableUser = findViewById<Button>(R.id.disablebtn)
        disableUser.setOnClickListener {
            disableUser()
        }

        val enableUser = findViewById<Button>(R.id.enablebtn)
        enableUser.setOnClickListener {
            enableUser()
        }

        val emailData = intent.getStringExtra("email")
        Log.d("Validation", "Email data received: $emailData")

        if (emailData != null) {
            val userRef = accountsRef.orderByChild("email").equalTo(emailData)

            val usernameEditText = findViewById<EditText>(R.id.username)
            val emailEditText = findViewById<EditText>(R.id.email)
            val phoneEditText = findViewById<EditText>(R.id.phone)
            val imageView = findViewById<ImageView>(R.id.image)

            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.d("Validation", "User data exists for email: $emailData")
                        for (userSnapshot in dataSnapshot.children) {
                            val user = userSnapshot.getValue(Users::class.java)
                            if (user != null) {
                                Log.d("Validation", "User data retrieved: ${user.username}")
                                usernameEditText.setText(user.username)
                                emailEditText.setText(user.email)
                                phoneEditText.setText(user.phone)
                                // Load image using Glide or Picasso based on user.imageUrl
                                if (user.imageUrl.isNotEmpty()) {
                                    Glide.with(this@Validation)
                                        .load(user.imageUrl)
                                        .into(imageView)
                                }
                            } else {
                                Log.d("Validation", "User data is null for snapshot: ${userSnapshot.key}")
                            }
                        }
                    } else {
                        Log.d("Validation", "No user found with email: $emailData")
                        Toast.makeText(this@Validation, "User not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Validation", "Database error: ${error.message}")
                    Toast.makeText(this@Validation, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Log.d("Validation", "No email data provided in the intent")
        }
    }

    private fun deleteUser() {
        val emailData = intent.getStringExtra("email")
        if (emailData != null) {
            val userRef = accountsRef.orderByChild("email").equalTo(emailData)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val userId = userSnapshot.key
                            if (userId != null) {
                                Log.d("DeleteUser", "Deleting user with ID: $userId")
                                accountsRef.child(userId).removeValue().addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d("DeleteUser", "User deleted successfully")
                                        Toast.makeText(this@Validation, "User deleted successfully", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Log.d("DeleteUser", "Failed to delete user: ${task.exception?.message}")
                                        Toast.makeText(this@Validation, "Failed to delete user", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Log.d("DeleteUser", "User ID is null for snapshot: ${userSnapshot.key}")
                            }
                        }
                    } else {
                        Log.d("DeleteUser", "User not found with email: $emailData")
                        Toast.makeText(this@Validation, "User not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("DeleteUser", "Database error: ${error.message}")
                    Toast.makeText(this@Validation, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Log.d("DeleteUser", "Email data is null")
        }
    }

    private fun disableUser() {
        val emailData = intent.getStringExtra("email")
        if (emailData != null) {
            val userRef = accountsRef.orderByChild("email").equalTo(emailData)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val userId = userSnapshot.key
                            if (userId != null) {
                                accountsRef.child(userId).child("disabled").setValue(true).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d("DisableUser", "User disabled successfully")
                                        Toast.makeText(this@Validation, "User disabled successfully", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Log.d("DisableUser", "Failed to disable user: ${task.exception?.message}")
                                        Toast.makeText(this@Validation, "Failed to disable user", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Log.d("DisableUser", "User ID is null for snapshot: ${userSnapshot.key}")
                            }
                        }
                    } else {
                        Log.d("DisableUser", "User not found with email: $emailData")
                        Toast.makeText(this@Validation, "User not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("DisableUser", "Database error: ${error.message}")
                    Toast.makeText(this@Validation, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Log.d("DisableUser", "Email data is null")
        }
    }

    private fun enableUser() {
        val emailData = intent.getStringExtra("email")
        if (emailData != null) {
            val userRef = accountsRef.orderByChild("email").equalTo(emailData)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val userId = userSnapshot.key
                            if (userId != null) {
                                accountsRef.child(userId).child("disabled").setValue(false).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this@Validation, "User enabled successfully", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(this@Validation, "Failed to enable user", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {

                            }
                        }
                    } else {
                        Toast.makeText(this@Validation, "User not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@Validation, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Log.d("DisableUser", "Email data is null")
        }
    }


}
