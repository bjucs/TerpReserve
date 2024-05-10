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
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

class LocationsActivity : AppCompatActivity() {
    // Declare arrays or lists of spinners and buttons
    private lateinit var dateSpinners: Array<Spinner>
    private lateinit var timeSpinners: Array<Spinner>
    private lateinit var reserveButtons: Array<Button>
    private var locations: Array<Location?> = arrayOfNulls(3)  // Assuming three locations
    private val locationKeys = listOf("location1", "location2", "location3")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.available_locations)

        // Initialize arrays
        dateSpinners = arrayOf(
            findViewById(R.id.dateSpinner1),
            findViewById(R.id.dateSpinner2),
            findViewById(R.id.dateSpinner3)
        )
        timeSpinners = arrayOf(
            findViewById(R.id.timeSpinner1),
            findViewById(R.id.timeSpinner2),
            findViewById(R.id.timeSpinner3)
        )
        reserveButtons = arrayOf(
            findViewById(R.id.reserveButton1),
            findViewById(R.id.reserveButton2),
            findViewById(R.id.reserveButton3)
        )

        locationKeys.forEachIndexed { index, key ->
            loadLocationData(key, index)
        }

        findViewById<Button>(R.id.accountButton).setOnClickListener {
            val profileIntent = Intent(this, UserProfileActivity::class.java)
            startActivity(profileIntent)
            finish()
        }
    }

    private fun loadLocationData(locationKey: String, index: Int) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("locations/$locationKey")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val location = snapshot.getValue(Location::class.java)
                location?.let {
                    locations[index] = it
                    updateDateSpinner(it.dates.keys.toList(), index)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("LocationActivity", "loadLocation:onCancelled", error.toException())
            }
        })
    }

    private fun updateDateSpinner(dates: List<String>, index: Int) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dates)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dateSpinners[index].adapter = adapter

        dateSpinners[index].onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                locations[index]?.dates?.get(dates[position])?.let {
                    updateTimeSpinner(it, index)
                }
                timeSpinners[index].isEnabled = true
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                timeSpinners[index].isEnabled = false
            }
        }
        reserveButtons[index].setOnClickListener {
            val selectedDate = dateSpinners[index].selectedItem as String
            val selectedTime = timeSpinners[index].selectedItem as String
            deleteReservation(locationKeys[index], selectedDate, selectedTime)
            addReservationToUser(locationKeys[index], selectedDate, selectedTime)
        }
    }

    private fun updateTimeSpinner(times: List<String>, index: Int) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, times)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timeSpinners[index].adapter = adapter
    }

    private fun deleteReservation(locationKey: String, selectedDate: String, selectedTime: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("locations/$locationKey/dates/$selectedDate")

        // Read the current times and remove the selected time
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val times = snapshot.getValue(object : GenericTypeIndicator<List<String>>() {}) ?: return

                // Remove the selected time from the list
                val updatedTimes = times.toMutableList().apply { remove(selectedTime) }

                // Update the times for the selected date
                if (updatedTimes.isEmpty()) {
                    // If no times left, remove the date entirely
                    databaseRef.removeValue()
                } else {
                    databaseRef.setValue(updatedTimes)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("LocationActivity", "Database error: $databaseError")
            }
        })
    }

    private fun addReservationToUser(locationKey: String, selectedDate: String, selectedTime: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val emailKey = sharedPreferences.getString("Email", null)
        val combinedReservationKey = selectedDate + selectedTime + locationKey

        val pathPrefix = "users/$emailKey/reservations/$combinedReservationKey"

        var databaseRef = FirebaseDatabase.getInstance().getReference("$pathPrefix/location")
        databaseRef.setValue(locationKey)
        databaseRef = FirebaseDatabase.getInstance().getReference("$pathPrefix/date")
        databaseRef.setValue(selectedDate)
        databaseRef = FirebaseDatabase.getInstance().getReference("$pathPrefix/time")
        databaseRef.setValue(selectedTime)
    }
}

