package com.example.ksheerasagara.data

import android.content.Context

object MilkCalendarStore {

    private const val PREF_NAME = "milk_calendar_store"
    private const val KEY_DATES = "milk_entry_dates"

    fun saveMilkEntryDate(context: Context, date: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val oldDates = prefs.getStringSet(KEY_DATES, emptySet()) ?: emptySet()
        val newDates = oldDates.toMutableSet()
        newDates.add(date)

        prefs.edit()
            .putStringSet(KEY_DATES, newDates)
            .apply()
    }

    fun getMilkEntryDates(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_DATES, emptySet()) ?: emptySet()
    }
}