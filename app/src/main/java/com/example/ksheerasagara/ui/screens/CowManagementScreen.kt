package com.example.ksheerasagara.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ksheerasagara.firebase.FirestoreManager
import org.json.JSONArray
import org.json.JSONObject

data class CowData(
    val name: String,
    val breed: String,
    val age: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CowManagementScreen(
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onExpenseClick: () -> Unit,
    onAIClick: () -> Unit
) {
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var cows by remember { mutableStateOf(getSavedCows(context)) }

    Scaffold(
        containerColor = Color(0xFFF1F8EA),
        topBar = {
            TopAppBar(
                title = { Text("Cow Management", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Add, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF145F1F)
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = false,
                    onClick = onHomeClick,
                    icon = { Icon(Icons.Default.GridView, null) },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onExpenseClick,
                    icon = { Icon(Icons.Default.ReceiptLong, null) },
                    label = { Text("Expenses") }
                )

                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Pets, null) },
                    label = { Text("Cows") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onAIClick,
                    icon = { Icon(Icons.Default.SmartToy, null) },
                    label = { Text("AI") }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color(0xFF2E963C),
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, null, tint = Color.White)
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "${cows.size} COWS REGISTERED",
                fontSize = 17.sp,
                color = Color(0xFF37474F)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn {
                items(cows) { cow ->
                    CowListCard(cow)
                }
            }
        }
    }

    if (showDialog) {
        AddCowDialog(
            onDismiss = { showDialog = false },
            onSave = { cow ->
                saveCowLocal(context, cow)

                FirestoreManager.saveCow(
                    name = cow.name,
                    breed = cow.breed,
                    age = cow.age
                )

                cows = getSavedCows(context)
                showDialog = false
            }
        )
    }
}

@Composable
fun CowListCard(
    cow: CowData
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFFE6F4E8), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("🐄")
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(cow.name, fontSize = 18.sp)
                Text("${cow.breed} · ${cow.age} yrs", fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun AddCowDialog(
    onDismiss: () -> Unit,
    onSave: (CowData) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Cow") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Cow Name") }
                )

                OutlinedTextField(
                    value = breed,
                    onValueChange = { breed = it },
                    label = { Text("Breed") }
                )

                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(
                            CowData(
                                name = name,
                                breed = breed.ifBlank { "Unknown" },
                                age = age.ifBlank { "0" }
                            )
                        )
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

fun saveCowLocal(
    context: Context,
    cow: CowData
) {
    val prefs = context.getSharedPreferences("cow_data", Context.MODE_PRIVATE)
    val oldJson = prefs.getString("cow_list", "[]") ?: "[]"
    val array = JSONArray(oldJson)

    val obj = JSONObject()
    obj.put("name", cow.name)
    obj.put("breed", cow.breed)
    obj.put("age", cow.age)

    array.put(obj)

    prefs.edit()
        .putString("cow_list", array.toString())
        .apply()
}

fun getSavedCows(
    context: Context
): List<CowData> {
    return try {
        val prefs = context.getSharedPreferences("cow_data", Context.MODE_PRIVATE)
        val json = prefs.getString("cow_list", "[]") ?: "[]"
        val array = JSONArray(json)
        val list = mutableListOf<CowData>()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)

            list.add(
                CowData(
                    name = obj.optString("name"),
                    breed = obj.optString("breed"),
                    age = obj.optString("age")
                )
            )
        }

        if (list.isEmpty()) {
            listOf(
                CowData("Lakshmi", "HF Breed", "3"),
                CowData("Savitri", "Jersey", "4"),
                CowData("Ganga", "Gir", "5"),
                CowData("Radha", "HF", "2")
            )
        } else {
            list
        }

    } catch (e: Exception) {
        emptyList()
    }
}