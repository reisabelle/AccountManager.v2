package com.example.accountmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class passConfirmation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass_confirmation)

        val originalPassword = intent.getStringExtra("PASSWORD_KEY") //get the password value from panel
        val originalEmail = intent.getStringExtra("EMAIL_KEY")

        val backbtn4: ImageView = findViewById(R.id.backbtn4)
        backbtn4.setOnClickListener(){
            val intent = Intent(this, panel::class.java)
            intent.putExtra("PASSWORD_KEY", originalPassword) //pass the password value
            intent.putExtra("EMAIL_KEY", originalEmail)
            startActivity(intent)
        }

        val passCon = findViewById<EditText>(R.id.passCon)
        val okbtn: Button = findViewById(R.id.okbtn)

        okbtn.setOnClickListener(){
            val confirmPassword = passCon.text.toString() //assigned the password value to variable

            if (confirmPassword == originalPassword) { // handle the password confirmation
                Toast.makeText(this, "Password confirmed successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, info::class.java)
                intent.putExtra("PASSWORD_KEY", originalPassword)
                intent.putExtra("EMAIL_KEY", originalEmail)
                startActivity(intent)
            }else if(confirmPassword.isEmpty()){
                Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
