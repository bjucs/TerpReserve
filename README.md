# TerpReserve

TerpReserve is an Android app designed to create and manage reservations for times and places on-campus at UMD, created as a student project for CMSC436 (Programming Handheld Devices) by Saahil Singh and Brian Ju. 

It's currently a minimum viable product (MVP) that reads/writes from user-created listing data in Firebase. 

Here's an example of how we add a user reservation to Firebase: 
```
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
```

And how we retrieve user reservations: 
```
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
```

