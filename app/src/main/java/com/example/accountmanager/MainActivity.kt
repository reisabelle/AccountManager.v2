package com.example.accountmanager

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val regbtn: Button = findViewById(R.id.regbtn)
        regbtn.setOnClickListener(){
            val intent = Intent(this, register::class.java)
            startActivity(intent)
        }

        val logbtn: Button = findViewById(R.id.acc)
        logbtn.setOnClickListener(){
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }
    }
}
