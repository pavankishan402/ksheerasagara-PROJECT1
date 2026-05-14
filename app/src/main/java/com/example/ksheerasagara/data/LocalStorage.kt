package com.example.ksheerasagara.data

import android.content.Context

object LocalStorage {

    private fun userKey(): String {
        return if (UserSession.userId.isBlank()) "default_user" else UserSession.userId
    }

    private fun prefs(context: Context) =
        context.getSharedPreferences(
            "milk_${userKey()}",
            Context.MODE_PRIVATE
        )

    fun saveMilkEntry(
        context: Context,
        cowName: String,
        liters: Double,
        fat: Double,
        snf: Double,
        rate: Double
    ) {
        val income = liters * rate

        val oldIncome = prefs(context).getFloat("total_income", 0f)
        val oldLiters = prefs(context).getFloat("total_liters", 0f)
        val oldFat = prefs(context).getFloat("total_fat", 0f)
        val oldSnf = prefs(context).getFloat("total_snf", 0f)
        val oldEntries = prefs(context).getInt("milk_entries", 0)

        prefs(context)
            .edit()
            .putFloat("total_income", (oldIncome + income).toFloat())
            .putFloat("total_liters", (oldLiters + liters).toFloat())
            .putFloat("total_fat", (oldFat + fat).toFloat())
            .putFloat("total_snf", (oldSnf + snf).toFloat())
            .putInt("milk_entries", oldEntries + 1)
            .apply()

        saveCowData(context, cowName, liters, income)
    }

    fun saveCowData(
        context: Context,
        cowName: String,
        liters: Double,
        income: Double
    ) {
        val oldCowLiters = prefs(context).getFloat("${cowName}_liters", 0f)
        val oldCowIncome = prefs(context).getFloat("${cowName}_income", 0f)

        prefs(context)
            .edit()
            .putFloat("${cowName}_liters", (oldCowLiters + liters).toFloat())
            .putFloat("${cowName}_income", (oldCowIncome + income).toFloat())
            .apply()
    }

    fun totalIncome(context: Context): Double =
        prefs(context).getFloat("total_income", 0f).toDouble()

    fun totalLiters(context: Context): Double =
        prefs(context).getFloat("total_liters", 0f).toDouble()

    fun avgFat(context: Context): Double {
        val totalFat = prefs(context).getFloat("total_fat", 0f)
        val entries = prefs(context).getInt("milk_entries", 0)
        return if (entries > 0) (totalFat / entries).toDouble() else 0.0
    }

    fun avgSnf(context: Context): Double {
        val totalSnf = prefs(context).getFloat("total_snf", 0f)
        val entries = prefs(context).getInt("milk_entries", 0)
        return if (entries > 0) (totalSnf / entries).toDouble() else 0.0
    }

    fun milkEntries(context: Context): Int =
        prefs(context).getInt("milk_entries", 0)

    fun cowIncome(context: Context, cowName: String): Double =
        prefs(context).getFloat("${cowName}_income", 0f).toDouble()

    fun cowLiters(context: Context, cowName: String): Double =
        prefs(context).getFloat("${cowName}_liters", 0f).toDouble()

    fun clearAll(context: Context) {
        prefs(context).edit().clear().apply()
    }
}