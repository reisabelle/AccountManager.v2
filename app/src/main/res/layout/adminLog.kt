package com.example.accountmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.chip.Chip

class adminLog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_log)

        val back: ImageView = findViewById(R.id.backbutton)
        back.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val username = findViewById<EditText>(R.id.adminUsername)
        val password = findViewById<EditText>(R.id.password)

        val adminLog: Button = findViewById(R.id.login)
        adminLog.setOnClickListener {
            val usernameText = username.text.toString()
            val passwordText = password.text.toString()

            if (usernameText == "admin" && passwordText == "management") {
                val intent = Intent(this, Admin::class.java)
                startActivity(intent)
            } else {
                if (usernameText.isEmpty() || passwordText.isEmpty()) {
                    Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
