package com.example.accountmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.accountmanager.ModelClassess.Account_Model
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.auth.FirebaseAuth

class update_delete : AppCompatActivity() {

    private lateinit var accountsRef: DatabaseReference
    private lateinit var auth: FirebaseAuth





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_delete)

        val originalPassword = intent.getStringExtra("PASSWORD_KEY")
        val originalEmail = intent.getStringExtra("EMAIL_KEY")

        val platform = findViewById<EditText>(R.id.platform)
        val email = findViewById<EditText>(R.id.email)


        val back = findViewById<ImageView>(R.id.backbtn3)
        back.setOnClickListener{
            val intent = Intent(this, info::class.java)
            intent.putExtra("PASSWORD_KEY", originalPassword)
            intent.putExtra("EMAIL_KEY", originalEmail)
            startActivity(intent)
        }

        val updateAccountData = findViewById<Button>(R.id.updatebtn)
        updateAccountData.setOnClickListener {
            updateAccountsData()
        }

        val deleteAccountData = findViewById<Button>(R.id.deletebtn)
        deleteAccountData.setOnClickListener {
            val platform = platform.text.toString().trim()
            val email = email.text.toString().trim()

            if (platform.isNotEmpty() && email.isNotEmpty()) {
                deleteAccountsData(platform, email)
            } else {
                Toast.makeText(this, "Platform and Email fields cannot be empty", Toast.LENGTH_SHORT).show()
            }

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
    private fun updateAccountsData() {
        accountsRef = FirebaseDatabase.getInstance().getReference("Accounts")
        val emailData = intent.getStringExtra("platform")

        if (emailData != null) {
            val userRef = accountsRef.orderByChild("platform").equalTo(emailData)
            val platform = findViewById<EditText>(R.id.platform)
            val link = findViewById<EditText>(R.id.link)
            val email = findViewById<EditText>(R.id.email)
            val phone = findViewById<EditText>(R.id.phoneNo)
            val password = findViewById<EditText>(R.id.acc_password)

            val updatedData = hashMapOf<String, Any>(
                "platform" to platform.text.toString().trim(),
                "link" to link.text.toString().trim(),
                "email" to email.text.toString().trim(),
                "phoneNo" to phone.text.toString().trim(),
                "password" to password.text.toString().trim()
            )

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (data in dataSnapshot.children) {
                            data.ref.updateChildren(updatedData).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this@update_delete, "Account data updated successfully", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@update_delete, "Failed to update account data", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener { exception ->
                                Toast.makeText(this@update_delete, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@update_delete, "Account data not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@update_delete, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Platform data not found", Toast.LENGTH_SHORT).show()
        }
    }
    private fun deleteAccountsData(platform: String, email: String) {
        val accountsRef = FirebaseDatabase.getInstance().getReference("Accounts")

        // Query based on the platform field
        val userRef = accountsRef.orderByChild("platform").equalTo(platform)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var recordFound = false
                if (dataSnapshot.exists()) {
                    for (data in dataSnapshot.children) {
                        val account = data.getValue(Account_Model::class.java)
                        // Check if the email matches
                        if (account?.email == email) {
                            val uniqueKey = data.key
                            if (uniqueKey != null) {
                                val specificRecordRef = accountsRef.child(uniqueKey)
                                specificRecordRef.removeValue().addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this@update_delete, "Account data deleted successfully", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(this@update_delete, "Failed to delete account data", Toast.LENGTH_SHORT).show()
                                    }
                                }.addOnFailureListener { exception ->
                                    Toast.makeText(this@update_delete, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                                }
                                recordFound = true
                                break
                            }
                        }
                    }
                    if (!recordFound) {
                        Toast.makeText(this@update_delete, "Record with email not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@update_delete, "Platform data not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@update_delete, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

