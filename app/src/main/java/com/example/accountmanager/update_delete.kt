package com.example.accountmanager

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.accountmanager.ModelClassess.Account_Model
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class update_delete : AppCompatActivity() {

    private lateinit var accountsRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_delete)

        val back = findViewById<ImageView>(R.id.backbtn3)
        back.setOnClickListener{
            val intent = Intent(this, info::class.java)
            startActivity(intent)
        }

        accountsRef = FirebaseDatabase.getInstance().getReference("Accounts")
        val emailData = intent.getStringExtra("platform")

        if (emailData != null) {
            val userRef = accountsRef.orderByChild("platform").equalTo(emailData)

            val platform = findViewById<EditText>(R.id.platform)
            val link = findViewById<EditText>(R.id.link)
            val email = findViewById<EditText>(R.id.email)
            val phone = findViewById<EditText>(R.id.phoneNo)
            val password = findViewById<EditText>(R.id.acc_password)

            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (dataSnapshot in dataSnapshot.children) {
                            val data = dataSnapshot.getValue(Account_Model::class.java)!!

                            platform.setText(data.platform)
                            link.setText(data.link)
                            email.setText(data.email)
                            phone.setText(data.phoneNo)
                            password.setText(data.password)
                        }
                    } else {
                        Toast.makeText(this@update_delete, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }
}
