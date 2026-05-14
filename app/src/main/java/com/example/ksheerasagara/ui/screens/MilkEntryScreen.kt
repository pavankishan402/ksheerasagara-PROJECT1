package com.example.ksheerasagara.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

@Composable
fun MilkEntryScreen() {

    var liters by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Milk Entry", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = liters,
            onValueChange = { liters = it },
            label = { Text("Liters") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = rate,
            onValueChange = { rate = it },
            label = { Text("Rate per Liter") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            val l = liters.toDoubleOrNull() ?: 0.0
            val r = rate.toDoubleOrNull() ?: 0.0

            val income = l * r
            result = "Income: ₹$income"
        }) {
            Text("Calculate")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(result)
    }
}