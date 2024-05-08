package com.groupproject.terpreserve
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Location(
    var name: String = "",
    var dates: MutableMap<String, MutableList<String>> = mutableMapOf()
) {
    // Method to add new times to a specific date or create a new date entry if it doesn't exist
    fun addDate(date: String, times: List<String>) {
        if (dates.containsKey(date)) {
            dates[date]?.addAll(times)
        } else {
            dates[date] = times.toMutableList()
        }
    }
}

