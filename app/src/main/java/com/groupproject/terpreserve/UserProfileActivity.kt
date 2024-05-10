package com.groupproject.terpreserve

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserProfileActivity : AppCompatActivity() {

    private lateinit var username : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val email = sharedPreferences.getString("Email", null)
        // Convert _ in email back to .
        val displayEmail = email!!.replace('_', '.')

        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile)

        findViewById<Button>(R.id.reserveButton).setOnClickListener {
            val locationsIntent = Intent(this, LocationsActivity::class.java)
            startActivity(locationsIntent)
            finish()
        }

        username = findViewById(R.id.userName)
        username.text = displayEmail

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
                // Failed to read value
                Log.w("UserProfileActivity", "Failed to read value.", databaseError.toException())
            }
        })

    }


}
