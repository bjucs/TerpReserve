package com.groupproject.terpreserve

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserProfileActivity : AppCompatActivity() {
    private lateinit var username : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile)
        createAd()

        // Convert _ in email back to . and update displayed username
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val email = sharedPreferences.getString("Email", null)
        val displayEmail = email!!.replace('_', '.')
        username = findViewById(R.id.userName)
        username.text = displayEmail

        // Redirect to reservation page
        findViewById<Button>(R.id.reserveButton).setOnClickListener {
            val locationsIntent = Intent(this, LocationsActivity::class.java)
            startActivity(locationsIntent)
            finish()
        }

        // Logout to the login screen
        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            logoutUser()
        }

        // Logic to retrieve user's reservations
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("users/$email/reservations")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val reservations = mutableListOf<Reservation>()
                for (snapshot in dataSnapshot.children) {
                    val reservation = snapshot.getValue(Reservation::class.java)
                    reservation?.let { reservations.add(it) }
                }
                val adapter = ReservationAdapter(this@UserProfileActivity, reservations)
                findViewById<ListView>(R.id.reservationsList).adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("UserProfileActivity", "Failed to read value.", databaseError.toException())
            }
        })

    }

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

    private fun createAd() {
        val adView = AdView(this)

        // Get the DisplayMetrics directly from resources
        val displayMetrics = resources.displayMetrics
        val widthPixels = displayMetrics.widthPixels
        val density = displayMetrics.density

        // Calculate the ad width based on the screen width
        val adWidth = (widthPixels / density).toInt()

        adView.setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth))
        adView.adUnitId = "ca-app-pub-3940256099942544/9214589741"

        val adRequest = AdRequest.Builder().build()
        findViewById<LinearLayout>(R.id.bannerAd).addView(adView)
        adView.loadAd(adRequest)
    }


}
