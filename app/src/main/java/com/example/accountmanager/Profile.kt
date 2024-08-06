package com.example.accountmanager

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class Profile : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    private lateinit var button: FloatingActionButton
    private lateinit var imageview: ImageView
    private var imageUri: Uri? = null

    private lateinit var imageView: ImageView
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var passwordEditText: EditText

    private var currentUsername: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val originalPassword = intent.getStringExtra("PASSWORD_KEY")
        val originalEmail = intent.getStringExtra("EMAIL_KEY")

        val back = findViewById<ImageView>(R.id.backbtn2)
        back.setOnClickListener {
            val intent = Intent(this, panel::class.java)
            intent.putExtra("PASSWORD_KEY", originalPassword)
            intent.putExtra("EMAIL_KEY", originalEmail)
            startActivity(intent)
            finish() // Close the current activity
        }

        val updateAcc = findViewById<Button>(R.id.updateAccbtn)
        updateAcc.setOnClickListener {
            updateUserData()
        }

        val deleteAcc = findViewById<Button>(R.id.deleteAccbtn)
        deleteAcc.setOnClickListener {
            deleteUserAccount()
        }

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("users")
        storage = FirebaseStorage.getInstance()

        // For Adding a profile picture
        imageview = findViewById(R.id.profile)
        button = findViewById(R.id.camera)

        button.setBackgroundColor(Color.TRANSPARENT)
        button.setOnClickListener {
            ImagePicker.with(this)
                .crop() // Crop image(Optional), Check Customization for more option
                .compress(1024) // Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080) // Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        // Initialize UI elements
        imageView = findViewById(R.id.profile)
        usernameEditText = findViewById(R.id.username)
        emailEditText = findViewById(R.id.email)
        phoneEditText = findViewById(R.id.phone)
        passwordEditText = findViewById(R.id.password)

        loadUserData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            imageview.setImageURI(imageUri)
            Log.d("ImagePicker", "Image selected: $imageUri")

            // Optionally, upload the image to Firebase Storage
            currentUsername?.let { username ->
                uploadImageToFirebase(imageUri, username)
            } ?: Toast.makeText(this, "Username not available", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Image selection failed", Toast.LENGTH_SHORT).show()
            Log.e("ImagePicker", "Image selection failed: resultCode=$resultCode, data=$data")
        }
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val username = snapshot.child("username").getValue(String::class.java)
                        val email = snapshot.child("email").getValue(String::class.java)
                        val phone = snapshot.child("phone").getValue(String::class.java)
                        val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)
                        val password = snapshot.child("password").getValue(String::class.java)

                        // Store the username for later use
                        currentUsername = username

                        // Set values to EditTexts
                        usernameEditText.setText(username)
                        emailEditText.setText(email)
                        phoneEditText.setText(phone)
                        passwordEditText.setText(password)

                        // Load image into ImageView
                        if (!imageUrl.isNullOrEmpty()) {
                            Picasso.get().load(imageUrl).into(imageView)
                        }
                    } else {
                        Toast.makeText(this@Profile, "User data not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Error fetching user data", error.toException())
                    Toast.makeText(this@Profile, "Error fetching user data", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUserData() {
        val userId = auth.currentUser?.uid
        val user = auth.currentUser

        if (userId != null && user != null) {
            val username = usernameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return
            }

            // upadte info in db
            val userUpdates = hashMapOf<String, Any>(
                "username" to username,
                "email" to email,
                "phone" to phone,
                "password" to password 
            )

            database.child(userId).updateChildren(userUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // update pass in firebase auth
                    user.updatePassword(password).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            Toast.makeText(this, "User data and password updated successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Failed to update user data", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri?, username: String) {
        if (imageUri == null) {
            Toast.makeText(this, "Image URI is null", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = auth.currentUser?.uid ?: return
        storageRef = storage.reference.child("profile_images/$username.jpg")

        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    database.child(userId).child("imageUrl").setValue(imageUrl)
                    Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseStorage", "Image upload failed", e)
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteUserAccount() {
        val userId = auth.currentUser?.uid
        val user = auth.currentUser

        if (user != null && userId != null) {
            // Delete the user data from the Realtime Database
            database.child(userId).removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Delete the user from Firebase Authentication
                    user.delete().addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            // User successfully deleted
                            Toast.makeText(this, "User account deleted successfully", Toast.LENGTH_SHORT).show()

                            // Optionally, navigate to another activity or log out
                            val intent = Intent(this, login::class.java)
                            startActivity(intent)
                            finish() // Close the current activity
                        } else {
                            Toast.makeText(this, "Failed to delete user account", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Failed to delete user data", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }
}