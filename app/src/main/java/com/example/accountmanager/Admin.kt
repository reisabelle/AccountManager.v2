package com.example.accountmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Admin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val rgstrdUser: Button = findViewById(R.id.rgstrdUser)
        rgstrdUser.setOnClickListener() {
            val intent = Intent(this, registeredUser::class.java)
            startActivity(intent)
        }

        val valbtn: Button = findViewById(R.id.valbtn)
        valbtn.setOnClickListener() {
            val intent = Intent(this, toValidate::class.java)
            startActivity(intent)
        }
    }
}
