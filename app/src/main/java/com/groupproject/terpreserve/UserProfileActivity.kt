package com.groupproject.terpreserve

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UserProfileActivity : AppCompatActivity() {

    private lateinit var username : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val email = sharedPreferences.getString("Email", null)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile)

        findViewById<Button>(R.id.reserveButton).setOnClickListener {
            val locationsIntent = Intent(this, LocationsActivity::class.java)
            startActivity(locationsIntent)
            finish()
        }

        username = findViewById(R.id.userName)
        username.text = email
    }


}
