package com.example.accountmanager

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.accountmanager.databinding.ActivityAccInfoBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class accInfo : AppCompatActivity() {


    private lateinit var binding: ActivityAccInfoBinding
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acc_info)

        binding = ActivityAccInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backbtn4: ImageView = findViewById(R.id.backbutton)
        backbtn4.setOnClickListener{
            val intent = Intent(this, panel::class.java)
            startActivity(intent)
        }

        val originalEmail = intent.getStringExtra("EMAIL_KEY")

        binding.button.setOnClickListener {
            val platform = binding.Platform.text.toString().trim()
            if (platform.isNotEmpty()) {
                readData(platform)
            } else {
                Toast.makeText(this, "Please Enter Platform", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun readData(platform: String) {
        reference = FirebaseDatabase.getInstance().getReference("Accounts")
        reference.child(platform).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                if (dataSnapshot!!.exists()) {
                    val email = dataSnapshot.child("email").value.toString()
                    val phoneNo = dataSnapshot.child("phoneNo").value.toString()
                    val password = dataSnapshot.child("password").value.toString()
                    Toast.makeText(this, "Read Data Successfully", Toast.LENGTH_SHORT).show()
                        binding.Email.text = email
                        binding.PhoneNo.text = phoneNo
                        binding.Password.text = password
                } else {
                    Toast.makeText(this, "Account Doesn't Exist", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Failed to read", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
