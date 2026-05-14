package com.example.ksheerasagara.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
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
import org.json.JSONArray

data class DailyMilkEntry(
    val date: String,
    val shift: String,
    val liter: Double,
    val fat: Double,
    val amount: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyPassbookScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val entries = remember { getDailyMilkEntries(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Passbook") },
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
                .padding(8.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Date")
                Text("Shift")
                Text("Ltr")
                Text("Fat")
                Text("Amount")
            }

            Divider()

            LazyColumn {
                items(entries) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item.date)
                        Text(item.shift)
                        Text("%.1f".format(item.liter))
                        Text("%.1f".format(item.fat))
                        Text(
                            "₹%.0f".format(item.amount),
                            color = Color(0xFF0B7A25)
                        )
                    }
                    Divider()
                }
            }
        }
    }
}

fun getDailyMilkEntries(context: Context): List<DailyMilkEntry> {
    return try {
        val prefs = context.getSharedPreferences("milk_entries", Context.MODE_PRIVATE)
        val json = prefs.getString("milk_list", "[]") ?: "[]"
        val array = JSONArray(json)

        val list = mutableListOf<DailyMilkEntry>()

        for (i in 0 until array.length()) {
            val obj = array.optJSONObject(i) ?: continue

            val liter = obj.optDouble("liters", 0.0)
            val fat = obj.optDouble("fat", 0.0)
            val rate = obj.optDouble("rate", 0.0)
            val amount = obj.optDouble("amount", liter * rate)

            list.add(
                DailyMilkEntry(
                    date = obj.optString("date", "Today"),
                    shift = obj.optString("shift", "Morning"),
                    liter = liter,
                    fat = fat,
                    amount = amount
                )
            )
        }

        list.reversed()

    } catch (e: Exception) {
        emptyList()
    }
}