package com.groupproject.terpreserve

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            // No user is signed in
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()  // Finish MainActivity so user can't return to it with the back button
        } else {
            // User is signed in
            // Update your UI here (display user's data, etc.)
        }
    }
}
