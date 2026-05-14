package com.example.ksheerasagara.data

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import org.json.JSONArray
import org.json.JSONObject

data class Expense(
    val cowName: String,
    val category: String,
    val amount: Double
)

object ExpenseData {

    val expenses = mutableStateListOf<Expense>()

    private fun userKey(): String {
        return if (UserSession.userId.isBlank())
            "default_user"
        else
            UserSession.userId
    }

    private fun prefs(context: Context) =
        context.getSharedPreferences(
            "expense_${userKey()}",
            Context.MODE_PRIVATE
        )

    fun load(context: Context) {
        expenses.clear()
        expenses.addAll(getAllExpenses(context))
    }

    fun addExpense(context: Context, expense: Expense) {
        val list = getAllExpenses(context).toMutableList()
        list.add(expense)
        saveAll(context, list)

        expenses.clear()
        expenses.addAll(list)
    }

    private fun saveAll(context: Context, list: List<Expense>) {
        val jsonArray = JSONArray()

        list.forEach {
            val obj = JSONObject()
            obj.put("cowName", it.cowName)
            obj.put("category", it.category)
            obj.put("amount", it.amount)
            jsonArray.put(obj)
        }

        prefs(context)
            .edit()
            .putString("expense_list", jsonArray.toString())
            .apply()
    }

    fun getAllExpenses(context: Context): List<Expense> {
        return try {
            val json =
                prefs(context).getString("expense_list", "[]") ?: "[]"

            val jsonArray = JSONArray(json)
            val list = mutableListOf<Expense>()

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)

                list.add(
                    Expense(
                        cowName = obj.optString("cowName", "Lakshmi"),
                        category = obj.optString("category", "Other"),
                        amount = obj.optDouble("amount", 0.0)
                    )
                )
            }

            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun totalExpense(): Double =
        expenses.sumOf { it.amount }

    fun entryCount(): Int =
        expenses.size

    fun getFodderExpense(): Double =
        expenses.filter { it.category == "Fodder" }.sumOf { it.amount }

    fun getMedicalExpense(): Double =
        expenses.filter { it.category == "Medical" }.sumOf { it.amount }

    fun getLaborExpense(): Double =
        expenses.filter { it.category == "Labor" }.sumOf { it.amount }

    fun getOtherExpense(): Double =
        expenses.filter { it.category == "Other" }.sumOf { it.amount }

    fun getCowExpense(cowName: String): Double =
        expenses.filter { it.cowName == cowName }.sumOf { it.amount }

    fun clearAll(context: Context) {
        prefs(context).edit().clear().apply()
        expenses.clear()
    }
}