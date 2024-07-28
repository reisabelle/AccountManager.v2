package com.example.accountmanager

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.accountmanager.home
import com.example.accountmanager.passConfirmation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class panel : AppCompatActivity() {
    private var refUsers: DatabaseReference? = null
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel)

        val originalPassword = intent.getStringExtra("PASSWORD_KEY")
        val originalEmail = intent.getStringExtra("EMAIL_KEY")

        val name: TextView = findViewById(R.id.name)
        val profile: ImageView = findViewById(R.id.profile)
        val eMail: TextView = findViewById(R.id.email)

        val button1: Button = findViewById(R.id.homebtn)
        button1.setBackgroundColor(Color.TRANSPARENT)
        button1.setOnClickListener {
            val intent = Intent(this, home::class.java)
            intent.putExtra("PASSWORD_KEY", originalPassword)
            intent.putExtra("EMAIL_KEY", originalEmail)
            startActivity(intent)
        }
        val button2: Button = findViewById(R.id.infobtn)
        button2.setBackgroundColor(Color.TRANSPARENT)
        button2.setOnClickListener {
            val intent = Intent(this, passConfirmation::class.java)
            intent.putExtra("PASSWORD_KEY", originalPassword)
            intent.putExtra("EMAIL_KEY", originalEmail)
            startActivity(intent)
        }
        val button3: Button = findViewById(R.id.logoutbtn)
        button3.setBackgroundColor(Color.TRANSPARENT)
        button3.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Initialize Firebase Auth and get the current user
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            Log.d("FirebaseAuth", "User ID: $userId")

            refUsers = FirebaseDatabase.getInstance().reference.child("users").child(userId)

            refUsers!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val email = snapshot.child("email").value?.toString()
                        val username = snapshot.child("username").value?.toString()
                        if (username.isNullOrEmpty()) {
                            Log.e("FirebaseDatabase", "Username is null or empty for User ID: $userId")
                            return
                        }
                        Log.d("FirebaseDatabase", "Username retrieved: $username")

                        name.text = username
                        eMail.text = email

                        // Get a reference to the user's profile picture using the retrieved username
                        val storageReference = FirebaseStorage.getInstance().reference
                        val profilePicRef = storageReference.child("profile_images/$username.jpg")

                        // Load the image into the ImageView using Picasso
                        profilePicRef.downloadUrl.addOnSuccessListener { uri ->
                            Log.d("FirebaseStorage", "Profile image URL: $uri")
                            Picasso.get().load(uri)

                                .into(profile)
                        }.addOnFailureListener { exception ->
                            Log.e("FirebaseStorage", "Error getting profile image URL", exception)
                        }
                    } else {
                        Log.e("FirebaseDatabase", "User data does not exist in database for User ID: $userId")
                        Log.d("FirebaseDatabase", "Snapshot data: ${snapshot.value}")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseDatabase", "Error fetching user data", error.toException())
                }
            })
        } else {
            Log.e("FirebaseAuth", "No current user found")
        }
    }
}