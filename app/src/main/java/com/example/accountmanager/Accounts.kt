package com.example.accountmanager

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Accounts : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_accounts)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val addbtn: ImageView = findViewById(R.id.addbtn)
        addbtn.setOnClickListener() {
            val intent = Intent(this, add_account::class.java)
            startActivity(intent)
        }

        val backbtn: ImageView = findViewById(R.id.backbtn)
        backbtn.setOnClickListener(){
            val intent = Intent(this, home::class.java)
            startActivity(intent)
        }
    }
}