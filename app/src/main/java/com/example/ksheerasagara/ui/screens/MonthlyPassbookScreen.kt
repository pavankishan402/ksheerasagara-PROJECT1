package com.example.ksheerasagara.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun MonthlyPassbookScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val report = remember { getMonthlyReport(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Monthly Passbook") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF78909C),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            MonthlyCard("Total Milk", "%.1f Liters".format(report.totalLiters))
            MonthlyCard("Average Fat", "%.1f%%".format(report.avgFat))
            MonthlyCard("Milk Income", "₹%.0f".format(report.income), Color(0xFF0B7A25))
            MonthlyCard("Total Expense", "₹%.0f".format(report.expense), Color.Red)

            val profitColor =
                if (report.profit >= 0) Color(0xFF0B7A25) else Color.Red

            MonthlyCard(
                title = "Monthly Profit / Loss",
                value = "₹%.0f".format(report.profit),
                valueColor = profitColor
            )

            MonthlyCard(
                title = "Earned By You",
                value = if (report.profit >= 0)
                    "You earned ₹%.0f this month".format(report.profit)
                else
                    "Loss ₹%.0f this month".format(kotlin.math.abs(report.profit)),
                valueColor = profitColor
            )
        }
    }
}

@Composable
fun MonthlyCard(
    title: String,
    value: String,
    valueColor: Color = Color.Black
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title)
            Text(value, color = valueColor)
        }
    }
}

data class MonthlyReport(
    val totalLiters: Double,
    val avgFat: Double,
    val income: Double,
    val expense: Double,
    val profit: Double
)

fun getMonthlyReport(context: Context): MonthlyReport {
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

        val expenses = try {
            ExpenseData.getAllExpenses(context)
        } catch (e: Exception) {
            emptyList()
        }

        val expense = expenses.sumOf { it.amount }
        val profit = income - expense

        MonthlyReport(
            totalLiters = liters,
            avgFat = if (fatCount > 0) fatTotal / fatCount else 0.0,
            income = income,
            expense = expense,
            profit = profit
        )

    } catch (e: Exception) {
        MonthlyReport(0.0, 0.0, 0.0, 0.0, 0.0)
    }
}