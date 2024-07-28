package com.example.accountmanager

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.accountmanager.R
import com.example.accountmanager.home
import com.example.accountmanager.passConfirmation

class panel : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel)

        val originalPassword = intent.getStringExtra("PASSWORD_KEY")//get the password value from home
        val originalEmail = intent.getStringExtra("EMAIL_KEY")

        val button1: Button = findViewById(R.id.homebtn)
        button1.setBackgroundColor(Color.TRANSPARENT)
        button1.setOnClickListener{
            val intent = Intent(this, home::class.java)
            intent.putExtra("PASSWORD_KEY", originalPassword) //pass the password value
            intent.putExtra("EMAIL_KEY", originalEmail)
            startActivity(intent)
        }
        val button2: Button = findViewById(R.id.infobtn)
        button2.setBackgroundColor(Color.TRANSPARENT)

        button2.setOnClickListener{
            val intent = Intent(this, passConfirmation::class.java)
            intent.putExtra("PASSWORD_KEY", originalPassword) //pass the password value
            intent.putExtra("EMAIL_KEY", originalEmail)
            startActivity(intent)
        }
        val button3: Button = findViewById(R.id.logoutbtn)
        button3.setBackgroundColor(Color.TRANSPARENT)
        button3.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
