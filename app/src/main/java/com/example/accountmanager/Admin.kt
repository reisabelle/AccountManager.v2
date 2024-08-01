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

         val toolbar = findViewById<MaterialToolbar>(R.id.materialToolbar2)
        setSupportActionBar(toolbar)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_logout -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
}
