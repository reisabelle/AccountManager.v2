package com.example.accountmanager

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class toValidate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_validate)

        val back: ImageView = findViewById(R.id.backbtn10)
        back.setOnClickListener{
            val intent = Intent(this, Admin::class.java)
            startActivity(intent)
        }
    }
}
