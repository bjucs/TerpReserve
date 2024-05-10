package com.groupproject.terpreserve

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val loggedIn = sharedPreferences.getBoolean("LoggedIn", false)

        if (!loggedIn) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        } else {
            // Transition to reservation page
            val locationsIntent = Intent(this, LocationsActivity::class.java)
            startActivity(locationsIntent)
            finish()
        }
    }
}
