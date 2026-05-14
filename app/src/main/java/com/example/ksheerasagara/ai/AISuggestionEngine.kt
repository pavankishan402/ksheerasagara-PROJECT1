package com.example.ksheerasagara.ai

import android.content.Context
import com.example.ksheerasagara.data.ExpenseData
import com.example.ksheerasagara.data.MilkData

object AISuggestionEngine {

    fun getSuggestion(context: Context): String {

        MilkData.load(context)
        ExpenseData.load(context)

        val totalIncome = MilkData.totalIncome()
        val totalExpense = ExpenseData.totalExpense()
        val totalLiters = MilkData.totalLiters()
        val avgFat = MilkData.avgFat()

        val profitLoss = totalIncome - totalExpense

        return when {
            totalIncome == 0.0 && totalExpense == 0.0 -> {
                "Start adding milk slips and expenses. I will analyze your dairy profit automatically."
            }

            profitLoss < 0 -> {
                "Your dairy is currently in loss of ₹${"%.0f".format(kotlin.math.abs(profitLoss))}. Try reducing fodder or medical expenses."
            }

            avgFat < 3.5 && totalLiters > 0 -> {
                "Average fat is low at ${"%.1f".format(avgFat)}%. Improve feed quality and check cow health."
            }

            totalExpense > totalIncome * 0.7 -> {
                "Expenses are high compared to income. Review fodder, labor, and medical spending."
            }

            totalLiters < 10 && totalIncome > 0 -> {
                "Milk quantity is low. Track each cow separately to identify low-yield cows."
            }

            else -> {
                "Your dairy performance looks good. Keep tracking milk and expenses daily."
            }
        }
    }
}