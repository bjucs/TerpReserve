package com.groupproject.terpreserve

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this)

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

    // Helper functions to reset Shared Preferences
    private fun clearSharedPreferences() {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    private fun restartApp() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun logoutUser() {
        clearSharedPreferences()
        restartApp()
    }

}
