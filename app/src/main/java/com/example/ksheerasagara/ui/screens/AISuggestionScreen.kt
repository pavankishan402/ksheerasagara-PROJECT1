package com.example.ksheerasagara.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ksheerasagara.data.ExpenseData
import org.json.JSONArray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AISuggestionScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    val suggestions = remember {
        generateAISuggestions(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Suggestions") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF145F1F),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF1F8EA)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Icon(
                imageVector = Icons.Default.SmartToy,
                contentDescription = null,
                modifier = Modifier.size(55.dp),
                tint = Color(0xFF145F1F)
            )

            Text(
                text = "Smart Suggestions",
                style = MaterialTheme.typography.headlineSmall
            )

            suggestions.forEach { text ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Text(
                        text = text,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

fun generateAISuggestions(context: Context): List<String> {
    val milkStats = readAIScreenMilkStats(context)

    val expenses = try {
        ExpenseData.getAllExpenses(context)
    } catch (e: Exception) {
        emptyList()
    }

    val totalExpense = expenses.sumOf { it.amount }
    val profit = milkStats.income - totalExpense

    val suggestions = mutableListOf<String>()

    if (milkStats.entries == 0) {
        suggestions.add("No milk slip added yet. Add today’s milk slip to get better suggestions.")
    } else {
        suggestions.add(
            "You sold %.1f liters milk and earned ₹%.0f.".format(
                milkStats.liters,
                milkStats.income
            )
        )
    }

    if (profit >= 0) {
        suggestions.add("Your current profit is ₹%.0f. Keep tracking expenses daily.".format(profit))
    } else {
        suggestions.add(
            "Your current loss is ₹%.0f. Reduce high expense categories first.".format(
                kotlin.math.abs(profit)
            )
        )
    }

    if (milkStats.avgFat < 4.0 && milkStats.entries > 0) {
        suggestions.add(
            "Average fat is low at %.1f%%. Improve fodder quality and check cow health.".format(
                milkStats.avgFat
            )
        )
    } else if (milkStats.entries > 0) {
        suggestions.add("Average fat is %.1f%%. Milk quality looks good.".format(milkStats.avgFat))
    }

    val fodderExpense = expenses
        .filter { it.category == "Fodder" }
        .sumOf { it.amount }

    if (fodderExpense > 0 && totalExpense > 0) {
        val percent = fodderExpense / totalExpense * 100
        suggestions.add(
            "Fodder expense is %.0f%% of total expense. Compare fodder cost with milk income.".format(
                percent
            )
        )
    }

    suggestions.add("Add milk slips cow-wise so you can find which cow gives better profit.")

    return suggestions
}

data class AIMilkStats(
    val liters: Double,
    val income: Double,
    val avgFat: Double,
    val entries: Int
)

fun readAIScreenMilkStats(context: Context): AIMilkStats {
    return try {
        val prefs = context.getSharedPreferences("milk_entries", Context.MODE_PRIVATE)
        val json = prefs.getString("milk_list", "[]") ?: "[]"
        val array = JSONArray(json)

        var liters = 0.0
        var income = 0.0
        var fatTotal = 0.0
        var fatCount = 0

        for (i in 0 until array.length()) {
            val obj = array.optJSONObject(i) ?: continue

            val l = obj.optDouble("liters", 0.0)
            val f = obj.optDouble("fat", 0.0)
            val rate = obj.optDouble("rate", 0.0)
            val amount = obj.optDouble("amount", l * rate)

            liters += l
            income += amount

            if (f > 0) {
                fatTotal += f
                fatCount++
            }
        }

        AIMilkStats(
            liters = liters,
            income = income,
            avgFat = if (fatCount > 0) fatTotal / fatCount else 0.0,
            entries = array.length()
        )

    } catch (e: Exception) {
        AIMilkStats(0.0, 0.0, 0.0, 0)
    }
}