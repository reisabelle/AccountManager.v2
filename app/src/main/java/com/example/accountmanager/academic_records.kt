package com.example.accountmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.accountmanager.ModelClassess.Account_Model
import com.example.accountmanager.ModelClassess.Records_Model
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class academic_records : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: record_Adapter
    private val database = FirebaseDatabase.getInstance()
    private val accountsRef = database.getReference("Academic_Record")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_academic_records)

        val originalPassword = intent.getStringExtra("PASSWORD_KEY")
        val originalEmail = intent.getStringExtra("EMAIL_KEY")

        val back = findViewById<ImageView>(R.id.backbtn)
        back.setOnClickListener {
            val intent = Intent(this, home::class.java)
            intent.putExtra("PASSWORD_KEY", originalPassword)
            intent.putExtra("EMAIL_KEY", originalEmail)
            startActivity(intent)
        }

        val add = findViewById<ImageView>(R.id.addbtn)
        add.setOnClickListener {
            val intent = Intent(this, add_record::class.java)
            intent.putExtra("PASSWORD_KEY", originalPassword)
            intent.putExtra("EMAIL_KEY", originalEmail)
            startActivity(intent)
        }

        listView = findViewById(R.id.LV_records)
        adapter = record_Adapter(this, mutableListOf())
        listView.adapter = adapter

        if (originalEmail != null) {
            val userEmail = originalEmail
            fetchAccountsData(userEmail)
        }

        listView.setOnItemClickListener { parent, view, position, id ->
            // Handle item click here
            val selectedAccount = adapter.getItem(position) as Records_Model
            // Start update_delete activity and pass account data
            val intent = Intent(this@academic_records, update_delete_record::class.java)
            intent.putExtra("PASSWORD_KEY", originalPassword)
            intent.putExtra("EMAIL_KEY", originalEmail)
            intent.putExtra("subject", selectedAccount.subject)
            startActivity(intent)
        }
    }

    private fun fetchAccountsData(userEmail: String) {
        val userAccountsRef = accountsRef.orderByChild("userEmail").equalTo(userEmail)
        userAccountsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val record = mutableListOf<Records_Model>()
                for (accountSnapshot in snapshot.children) {
                    val records = accountSnapshot.getValue(Records_Model::class.java)
                    records?.let { record.add(it) }
                }
                adapter.updateData(record)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error here
            }
        })
    }
}
