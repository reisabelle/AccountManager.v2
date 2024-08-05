package com.example.accountmanager

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class academic_records : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_academic_records)

        val add = findViewById<ImageView>(R.id.addbtn)
        add.setOnClickListener{
            val intent = Intent(this, add_record::class.java)
            startActivity(intent)
        }
    }
}
