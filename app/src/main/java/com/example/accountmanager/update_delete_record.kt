package com.example.accountmanager

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.accountmanager.ModelClassess.Account_Model
import com.example.accountmanager.ModelClassess.Records_Model
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class update_delete_record : AppCompatActivity() {

    private lateinit var accountsRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_delete_record)

        val originalPassword = intent.getStringExtra("PASSWORD_KEY")
        val originalEmail = intent.getStringExtra("EMAIL_KEY")

        val back = findViewById<ImageView>(R.id.backbtn)
        back.setOnClickListener{
            val intent = Intent(this, academic_records::class.java)
            intent.putExtra("PASSWORD_KEY", originalPassword)
            intent.putExtra("EMAIL_KEY", originalEmail)
            startActivity(intent)
        }
        accountsRef = FirebaseDatabase.getInstance().getReference("Academic_Record")
        val subject = intent.getStringExtra("subject")

        if (subject != null) {
            val userRef = accountsRef.orderByChild("subject").equalTo(subject)

            val semester = findViewById<EditText>(R.id.semester)
            val subject = findViewById<EditText>(R.id.subject)
            val grade = findViewById<EditText>(R.id.grade)
            val instructor = findViewById<EditText>(R.id.instructor)

            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (dataSnapshot in dataSnapshot.children) {
                            val data = dataSnapshot.getValue(Records_Model::class.java)!!

                            semester.setText(data.semester)
                            subject.setText(data.subject)
                            grade.setText(data.grade)
                            instructor.setText(data.instructor)
                        }
                    } else {
                        Toast.makeText(this@update_delete_record, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }
}
