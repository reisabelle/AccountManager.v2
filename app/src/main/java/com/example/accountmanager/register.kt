package com.example.accountmanager

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
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
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    private lateinit var button: FloatingActionButton
    private lateinit var imageview: ImageView
    private var imageUri: Uri? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference.child("profile_images")

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backbtn2: ImageView = findViewById(R.id.backbtn2)
        backbtn2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val reg: Button = findViewById(R.id.Regbtn)
        val phoneEditText: EditText = findViewById(R.id.phone)
        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val usernameEditText: EditText = findViewById(R.id.username)

        reg.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val username = usernameEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || username.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                if (imageUri != null) {
                    uploadImageAndRegisterUser(username, email, phone, password, imageUri!!)
                } else {
                    Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            imageview.setImageURI(imageUri)
            Log.d("ImagePicker", "Image selected: $imageUri")
        } else {
            Toast.makeText(this, "Image selection failed", Toast.LENGTH_SHORT).show()
            Log.e("ImagePicker", "Image selection failed: resultCode=$resultCode, data=$data")
        }
    }

    private fun uploadImageAndRegisterUser(username: String, email: String, phone: String, password: String, imageUri: Uri) {
        Log.d("UploadImage", "Starting image upload: $imageUri")
        val fileRef = storageRef.child("$username.jpg")
        fileRef.putFile(imageUri)
            .addOnSuccessListener {
                Log.d("UploadImage", "Image uploaded successfully")
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    Log.d("UploadImage", "Image URL obtained: $imageUrl")
                    registerUser(username, email, phone, password, imageUrl)
                }.addOnFailureListener { e ->
                    Log.e("UploadImage", "Failed to get download URL: ${e.message}")
                    Toast.makeText(this, "Failed to get download URL: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("UploadImage", "Image upload failed: ${e.message}")
                Toast.makeText(this, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun registerUser(username: String, email: String, phone: String, password: String, imageUrl: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        it.sendEmailVerification()
                            .addOnCompleteListener { emailTask ->
                                if (emailTask.isSuccessful) {
                                    val userId = it.uid
                                    val userMap = mapOf(
                                        "username" to username,
                                        "email" to email,
                                        "phone" to phone,
                                        "imageUrl" to imageUrl,
                                        "isEmailVerified" to true // To track email verification status
                                    )

                                    database.child("users").child(userId).setValue(userMap)
                                        .addOnCompleteListener { dbTask ->
                                            if (dbTask.isSuccessful) {
                                                Toast.makeText(this, "Registration Successful. Please check your email for verification.", Toast.LENGTH_SHORT).show()
                                                val intent = Intent(this, login::class.java)
                                                startActivity(intent)
                                                finish()
                                            } else {
                                                Log.e("DatabaseError", "Database Error: ${dbTask.exception?.message}")
                                                Toast.makeText(this, "Database Error: ${dbTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                                dbTask.exception?.printStackTrace()
                                            }
                                        }
                                } else {
                                    Log.e("EmailError", "Email Verification Failed: ${emailTask.exception?.message}")
                                    Toast.makeText(this, "Failed to send verification email: ${emailTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    Log.e("AuthError", "Authentication Failed: ${task.exception?.message}")
                    Toast.makeText(this, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    task.exception?.printStackTrace()
                }
            }
    }
}
