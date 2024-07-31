package com.example.accountmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.accountmanager.ModelClassess.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class registeredUser : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: user_Adapter
    private val database = FirebaseDatabase.getInstance()
    private val accountsRef = database.getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registered_user)

        val back: ImageView = findViewById(R.id.backbtn9)
        back.setOnClickListener{
            val intent = Intent(this, Admin::class.java)
            startActivity(intent)
        }

        listView = findViewById(R.id.LV_users)
        adapter = user_Adapter(this, mutableListOf())
        listView.adapter = adapter

        fetchAccountsData()
    }

    private fun fetchAccountsData() {
        accountsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<Users>()
                for (accountSnapshot in snapshot.children) {
                    val account = accountSnapshot.getValue(Users::class.java)
                    account?.let { users.add(it) }
                }
                Log.d("FetchAccountsData", "Users fetched: ${users.size}")
                adapter.updateData(users)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FetchAccountsData", "Error fetching data", error.toException())
            }
        })
    }
}
