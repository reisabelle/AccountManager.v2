package com.example.accountmanager

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Panel
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        
        val back = findViewById<ImageView>(R.id.backbtn2)
        back.setOnClickListener{
            val intent = Intent(this, Panel::class.java)
            startActivity(intent)
        }
    }
}
