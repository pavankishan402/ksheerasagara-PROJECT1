package com.example.ksheerasagara.firebase

import com.example.ksheerasagara.data.UserSession
import com.google.firebase.database.FirebaseDatabase

object RealtimeDatabaseManager {

    private val database = FirebaseDatabase.getInstance().reference

    private fun userId(): String {
        return if (UserSession.userId.isNotBlank()) {
            UserSession.userId.replace(".", "_")
                .replace("#", "_")
                .replace("$", "_")
                .replace("[", "_")
                .replace("]", "_")
        } else {
            "demo_user"
        }
    }

    fun saveMilkSlip(
        date: String,
        shift: String,
        cowName: String,
        liters: Double,
        fat: Double,
        snf: Double,
        rate: Double,
        amount: Double
    ) {
        val id = database.child("users").child(userId()).child("milkSlips").push().key ?: return

        val data = mapOf(
            "date" to date,
            "shift" to shift,
            "cowName" to cowName,
            "liters" to liters,
            "fat" to fat,
            "snf" to snf,
            "rate" to rate,
            "amount" to amount,
            "timestamp" to System.currentTimeMillis()
        )

        database.child("users").child(userId()).child("milkSlips").child(id).setValue(data)
    }

    fun saveExpense(
        cowName: String,
        category: String,
        amount: Double
    ) {
        val id = database.child("users").child(userId()).child("expenses").push().key ?: return

        val data = mapOf(
            "cowName" to cowName,
            "category" to category,
            "amount" to amount,
            "timestamp" to System.currentTimeMillis()
        )

        database.child("users").child(userId()).child("expenses").child(id).setValue(data)
    }

    fun saveCow(
        name: String,
        breed: String,
        age: String
    ) {
        val id = database.child("users").child(userId()).child("cows").push().key ?: return

        val data = mapOf(
            "name" to name,
            "breed" to breed,
            "age" to age,
            "timestamp" to System.currentTimeMillis()
        )

        database.child("users").child(userId()).child("cows").child(id).setValue(data)
    }
}