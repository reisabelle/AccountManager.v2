package com.example.accountmanager

import ListToBeValidated
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class accValidation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_acc_validation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val okbtn2: Button = findViewById(R.id.okbtn2)
        okbtn2.setOnClickListener() {
            val intent = Intent(this, ListToBeValidated::class.java)
            startActivity(intent)
        }
        val backbtn7: ImageView = findViewById(R.id.backbtn7)
        backbtn7.setOnClickListener() {
            val intent = Intent(this, ListToBeValidated::class.java)
            startActivity(intent)
        }

    }
}