package com.example.ksheerasagara.data

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import org.json.JSONArray
import org.json.JSONObject

data class MilkSlip(
    val cowName: String,
    val liters: Double,
    val fat: Double,
    val snf: Double,
    val rate: Double,
    val income: Double
)

object MilkData {

    val slips = mutableStateListOf<MilkSlip>()

    private fun userKey(): String {
        return if (UserSession.userId.isBlank())
            "default_user"
        else
            UserSession.userId
    }

    private fun prefs(context: Context) =
        context.getSharedPreferences(
            "milk_${userKey()}",
            Context.MODE_PRIVATE
        )

    fun load(context: Context) {
        slips.clear()
        slips.addAll(getAllSlips(context))
    }

    fun addSlip(context: Context, slip: MilkSlip) {
        val list = getAllSlips(context).toMutableList()
        list.add(slip)
        saveAll(context, list)

        slips.clear()
        slips.addAll(list)
    }

    // Old support
    fun addSlip(slip: MilkSlip) {
        slips.add(slip)
    }

    private fun saveAll(context: Context, list: List<MilkSlip>) {
        val jsonArray = JSONArray()

        list.forEach {
            val obj = JSONObject()
            obj.put("cowName", it.cowName)
            obj.put("liters", it.liters)
            obj.put("fat", it.fat)
            obj.put("snf", it.snf)
            obj.put("rate", it.rate)
            obj.put("income", it.income)
            jsonArray.put(obj)
        }

        prefs(context)
            .edit()
            .putString("milk_list", jsonArray.toString())
            .apply()
    }

    fun getAllSlips(context: Context): List<MilkSlip> {
        return try {
            val json =
                prefs(context).getString("milk_list", "[]") ?: "[]"

            val jsonArray = JSONArray(json)
            val list = mutableListOf<MilkSlip>()

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)

                list.add(
                    MilkSlip(
                        cowName = obj.optString("cowName", "Lakshmi"),
                        liters = obj.optDouble("liters", 0.0),
                        fat = obj.optDouble("fat", 0.0),
                        snf = obj.optDouble("snf", 0.0),
                        rate = obj.optDouble("rate", 0.0),
                        income = obj.optDouble("income", 0.0)
                    )
                )
            }

            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun totalIncome(): Double =
        slips.sumOf { it.income }

    fun totalLiters(): Double =
        slips.sumOf { it.liters }

    fun avgFat(): Double =
        if (slips.isEmpty()) 0.0 else slips.map { it.fat }.average()

    fun entryCount(): Int =
        slips.size

    fun incomeByCow(cowName: String): Double =
        slips.filter { it.cowName == cowName }.sumOf { it.income }

    fun litersByCow(cowName: String): Double =
        slips.filter { it.cowName == cowName }.sumOf { it.liters }

    fun clearAll(context: Context) {
        prefs(context).edit().clear().apply()
        slips.clear()
    }
}