package com.groupproject.terpreserve

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LocationsActivity : AppCompatActivity() {
    private lateinit var dateSpinner: Spinner
    private lateinit var timeSpinner: Spinner
    private var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.available_locations)

        dateSpinner = findViewById(R.id.dateSpinner1)
        timeSpinner = findViewById(R.id.timeSpinner1)
        timeSpinner.isEnabled = false  // Disable timeSpinner initially

        val locationKeys = listOf("location1", "location2", "location3")
        for (locationKey in locationKeys) {
            loadLocationData(locationKey)
        }

        findViewById<Button>(R.id.accountButton).setOnClickListener {
            val profileIntent = Intent(this, UserProfileActivity::class.java)
            startActivity(profileIntent)
            finish()
        }
    }

    private fun loadLocationData(locationKey: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("locations/$locationKey")
        databaseRef.addListenerForSingleValueEvent(LocationDataListener())
    }

    inner class LocationDataListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            location = snapshot.getValue(Location::class.java)
            location?.let {
                updateDateSpinner(it.dates.keys.toList())
            }
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle possible errors
            Log.w("LocationActivity", "loadLocation:onCancelled", error.toException())
        }
    }

    private fun updateDateSpinner(dates: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dates)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dateSpinner.adapter = adapter
        dateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                location?.dates?.get(dates[position])?.let {
                    updateTimeSpinner(it)
                }
                timeSpinner.isEnabled = true
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                timeSpinner.isEnabled = false
            }
        }
    }

    private fun updateTimeSpinner(times: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, times)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timeSpinner.adapter = adapter
    }

}
