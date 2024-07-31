package com.example.accountmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class registeredUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registered_user)

        val back: ImageView = findViewById(R.id.backbtn9)
        back.setOnClickListener{
            val intent = Intent(this, Admin::class.java)
            startActivity(intent)
        }
    }
}
