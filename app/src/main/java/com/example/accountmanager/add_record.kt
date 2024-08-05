package com.example.accountmanager

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.accountmanager.databinding.ActivityAddAccountBinding
import com.example.accountmanager.databinding.ActivityAddRecordBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class add_record : AppCompatActivity() {

    private lateinit var binding: ActivityAddRecordBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_record)

        binding = ActivityAddRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val originalPassword = intent.getStringExtra("PASSWORD_KEY")
        val originalEmail = intent.getStringExtra("EMAIL_KEY")

        val back = findViewById<ImageView>(R.id.backbtn)
        back.setOnClickListener{
            val intent = Intent(this, academic_records::class.java)
            intent.putExtra("PASSWORD_KEY", originalPassword)
            intent.putExtra("EMAIL_KEY", originalEmail)
            startActivity(intent)
        }

        binding.submitbtn.setOnClickListener{
            val semester = binding.semester.text.toString().trim()
            val subject = binding.subject.text.toString().trim()
            val grade = binding.grade.text.toString().trim()
            val instructor = binding.instructor.text.toString().trim()
            val userEmail = originalEmail

            database = FirebaseDatabase.getInstance().reference
            if (semester.isEmpty() || subject.isEmpty() || grade.isEmpty() || instructor.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                val record = hashMapOf(
                    "semester" to semester,
                    "subject" to subject,
                    "grade" to grade,
                    "instructor" to instructor,
                    "userEmail" to userEmail
                )
                database.child("Academic_Record").push().setValue(record)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, academic_records::class.java)
                        intent.putExtra("PASSWORD_KEY", originalPassword)
                        intent.putExtra("EMAIL_KEY", originalEmail)
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
