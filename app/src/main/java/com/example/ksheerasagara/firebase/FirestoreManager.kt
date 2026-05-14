package com.example.ksheerasagara.firebase

import com.example.ksheerasagara.data.UserSession
import com.google.firebase.database.FirebaseDatabase

object FirestoreManager {

    // REALTIME DATABASE CONNECTION

    private val db = FirebaseDatabase
        .getInstance(
            "https://ksheerasagara-edc82-default-rtdb.asia-southeast1.firebasedatabase.app"
        )
        .reference

    // USER ID

    private fun userId(): String {

        return UserSession.userId
            .ifBlank { "demo_user" }
            .replace(".", "_")
            .replace("#", "_")
            .replace("$", "_")
            .replace("[", "_")
            .replace("]", "_")
    }

    // SAVE COW

    fun saveCow(
        name: String,
        breed: String,
        age: String
    ) {

        val cowId = db
            .child("users")
            .child(userId())
            .child("cows")
            .push()
            .key ?: return

        val data = mapOf(

            "name" to name,
            "breed" to breed,
            "age" to age,

            "timestamp" to System.currentTimeMillis()
        )

        db.child("users")
            .child(userId())
            .child("cows")
            .child(cowId)
            .setValue(data)
    }

    // SAVE MILK SLIP

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

        val milkId = db
            .child("users")
            .child(userId())
            .child("milkSlips")
            .push()
            .key ?: return

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

        db.child("users")
            .child(userId())
            .child("milkSlips")
            .child(milkId)
            .setValue(data)
    }

    // SAVE EXPENSE

    fun saveExpense(
        cowName: String,
        category: String,
        amount: Double
    ) {

        val expenseId = db
            .child("users")
            .child(userId())
            .child("expenses")
            .push()
            .key ?: return

        val data = mapOf(

            "cowName" to cowName,
            "category" to category,
            "amount" to amount,

            "timestamp" to System.currentTimeMillis()
        )

        db.child("users")
            .child(userId())
            .child("expenses")
            .child(expenseId)
            .setValue(data)
    }
}