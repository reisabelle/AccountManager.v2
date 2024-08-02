package com.example.accountmanager

import Adapter
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.accountmanager.ModelClassess.Account_Model
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class info : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: Adapter
    private val database = FirebaseDatabase.getInstance()
    private val accountsRef = database.getReference("Accounts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val originalPassword = intent.getStringExtra("PASSWORD_KEY") //get the password value from panel
        val originalEmail = intent.getStringExtra("EMAIL_KEY")

        val infobtn: ImageView = findViewById(R.id.backbtn5)
        infobtn.setOnClickListener () {
            val intent = Intent(this, panel::class.java)
            intent.putExtra("PASSWORD_KEY", originalPassword)
            intent.putExtra("EMAIL_KEY", originalEmail)
            startActivity(intent)
        }
        listView = findViewById(R.id.LV_info)
        adapter = Adapter(this, mutableListOf())
        listView.adapter = adapter

        if (originalEmail != null) {
            val userEmail = originalEmail
            fetchAccountsData(userEmail)
        }
    }
    private fun fetchAccountsData(userEmail: String) {
        val userAccountsRef = accountsRef.orderByChild("userEmail").equalTo(userEmail)
        userAccountsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val accounts = mutableListOf<Account_Model>()
                for (accountSnapshot in snapshot.children) {
                    val account = accountSnapshot.getValue(Account_Model::class.java)
                    account?.let { accounts.add(it) }
                }
                adapter.updateData(accounts)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error here
            }
        })
    }
}
