package com.example.accountmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val accbtn: Button = findViewById(R.id.accbtn)
        accbtn.setOnClickListener() {
            val intent = Intent(this, Accounts::class.java)
            startActivity(intent)
        }
        val menu: ImageView = findViewById(R.id.menu)
        menu.setOnClickListener() {
            val intent = Intent(this, panel::class.java)
            startActivity(intent)
        }
    }
}