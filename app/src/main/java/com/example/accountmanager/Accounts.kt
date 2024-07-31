package com.example.accountmanager

import Adapter
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import link_Adapter

class Accounts : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: link_Adapter
    private val database = FirebaseDatabase.getInstance()
    private val accountsRef = database.getReference("Accounts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounts)


        val originalPassword = intent.getStringExtra("PASSWORD_KEY")
        val originalEmail = intent.getStringExtra("EMAIL_KEY")

        val addbtn: ImageView = findViewById(R.id.addbtn)
        addbtn.setOnClickListener {
            val intent = Intent(this, add_account::class.java)
            intent.putExtra("PASSWORD_KEY", originalPassword)
            intent.putExtra("EMAIL_KEY", originalEmail)
            startActivity(intent)
        }

        val backbtn: ImageView = findViewById(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, home::class.java)
            intent.putExtra("PASSWORD_KEY", originalPassword)
            intent.putExtra("EMAIL_KEY", originalEmail)
            startActivity(intent)
        }

        listView = findViewById(R.id.LV_link)
        adapter = link_Adapter(this, mutableListOf())
        listView.adapter = adapter

        fetchAccountsData()

        listView.setOnItemClickListener { _, view, position, _ ->
            val account = adapter.getItem(position) as Account_Model
            val link = account.link

            // Check if link is empty
            if (link.isEmpty()) {
                Toast.makeText(this, "Invalid Link", Toast.LENGTH_SHORT).show()
                return@setOnItemClickListener
            }

            // Check if link starts with "http" (already existing check)
            if (link.isNotEmpty() && link.startsWith("http")) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            } else {
                // Handle link without "http" prefix (e.g., facebook.com, tiktok.com)
                val completeUrl = "https://$link" // Add "https://" prefix
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(completeUrl))
                startActivity(intent)
            }
        }
    }

    private fun fetchAccountsData() {
        accountsRef.addValueEventListener(object : ValueEventListener {
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
