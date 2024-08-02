package com.example.accountmanager

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.accountmanager.ModelClassess.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class validation : AppCompatActivity() {

    private lateinit var accountsRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validation)

        accountsRef = FirebaseDatabase.getInstance().getReference("users")

        val emailData = intent.getStringExtra("email")

        if (emailData != null) {
            val userRef = accountsRef.orderByChild("email").equalTo(emailData)

            val usernameEditText = findViewById<EditText>(R.id.username)
            val emailEditText = findViewById<EditText>(R.id.email)
            val phoneEditText = findViewById<EditText>(R.id.phone)
            val imageView = findViewById<ImageView>(R.id.image)

            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val user = userSnapshot.getValue(Users::class.java)!!

                            usernameEditText.setText(user.username)
                            emailEditText.setText(user.email)
                            phoneEditText.setText(user.phone)
                            // Load image using Glide or Picasso based on user.imageUrl
                            if (user.imageUrl.isNotEmpty()) {
                                Glide.with(this@validation)
                                    .load(user.imageUrl)
                                    .into(imageView)
                            }
                        }
                    } else {
                        // Handle case when user not found
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }
    }
