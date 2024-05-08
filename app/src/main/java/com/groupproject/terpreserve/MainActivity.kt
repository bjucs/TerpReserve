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
            finish()  // Finish MainActivity so user can't return to it with the back button
        } else {
            // Transition to reservation page
        }
    }
}
