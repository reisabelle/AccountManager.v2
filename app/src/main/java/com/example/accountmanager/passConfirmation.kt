package com.example.accountmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class passConfirmation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pass_confirmation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val backbtn4: ImageView = findViewById(R.id.backbtn4)
        backbtn4.setOnClickListener(){
            val intent = Intent(this, panel::class.java)
            startActivity(intent)
        }
        val okbtn: Button = findViewById(R.id.okbtn)
        okbtn.setOnClickListener(){
            val intent = Intent(this, info::class.java)
            startActivity(intent)
        }
    }
}