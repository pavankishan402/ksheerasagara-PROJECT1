package com.example.ksheerasagara.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMilkEntryScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val cowList = remember {
        getSavedCows(context).map { "${it.name} (${it.breed})" }
    }

    var selectedCow by remember {
        mutableStateOf(cowList.firstOrNull() ?: "No Cow Added")
    }

    var liters by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var snf by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("35") }
    var shift by remember { mutableStateOf("Morning") }

    var cowExpanded by remember { mutableStateOf(false) }
    var shiftExpanded by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    val shiftList = listOf("Morning", "Evening")

    val today = remember {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
    }

    val literValue = liters.toDoubleOrNull() ?: 0.0
    val fatValue = fat.toDoubleOrNull() ?: 0.0
    val snfValue = snf.toDoubleOrNull() ?: 0.0
    val rateValue = rate.toDoubleOrNull() ?: 0.0
    val amount = literValue * rateValue

    Scaffold(
        containerColor = Color(0xFFF1F8EA),
        topBar = {
            TopAppBar(
                title = { Text("Add Milk Slip", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF145F1F)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            MilkFieldCard(
                title = "DATE",
                value = today,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MilkFieldCard(
                    title = "LITERS",
                    value = liters,
                    onValueChange = { liters = it },
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Number
                )

                MilkFieldCard(
                    title = "FAT %",
                    value = fat,
                    onValueChange = { fat = it },
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Number
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MilkFieldCard(
                    title = "SNF %",
                    value = snf,
                    onValueChange = { snf = it },
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Number
                )

                MilkFieldCard(
                    title = "RATE / LITER ₹",
                    value = rate,
                    onValueChange = { rate = it },
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Number
                )
            }

            ExposedDropdownMenuBox(
                expanded = cowExpanded,
                onExpandedChange = { cowExpanded = !cowExpanded }
            ) {
                OutlinedTextField(
                    value = selectedCow,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("COW") },
                    trailingIcon = {
                        Icon(Icons.Default.KeyboardArrowDown, null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                ExposedDropdownMenu(
                    expanded = cowExpanded,
                    onDismissRequest = { cowExpanded = false }
                ) {
                    cowList.forEach { cow ->
                        DropdownMenuItem(
                            text = { Text(cow) },
                            onClick = {
                                selectedCow = cow
                                cowExpanded = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = shiftExpanded,
                onExpandedChange = { shiftExpanded = !shiftExpanded }
            ) {
                OutlinedTextField(
                    value = shift,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("SHIFT") },
                    trailingIcon = {
                        Icon(Icons.Default.KeyboardArrowDown, null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                ExposedDropdownMenu(
                    expanded = shiftExpanded,
                    onDismissRequest = { shiftExpanded = false }
                ) {
                    shiftList.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                shift = item
                                shiftExpanded = false
                            }
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE6F4E8)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Calculated Income",
                        color = Color(0xFF0B7A25),
                        fontSize = 14.sp
                    )

                    Text(
                        "₹ %.2f".format(amount),
                        color = Color(0xFF0B7A25),
                        fontSize = 22.sp
                    )
                }
            }

            Button(
                onClick = {
                    if (selectedCow == "No Cow Added") {
                        message = "Please add cow first"
                        return@Button
                    }

                    if (literValue <= 0 || rateValue <= 0) {
                        message = "Enter valid liters and rate"
                        return@Button
                    }

                    saveMilkSlipLocal(
                        context = context,
                        date = today,
                        shift = shift,
                        cowName = selectedCow,
                        liters = literValue,
                        fat = fatValue,
                        snf = snfValue,
                        rate = rateValue,
                        amount = amount
                    )

                    FirestoreManager.saveMilkSlip(
                        date = today,
                        shift = shift,
                        cowName = selectedCow,
                        liters = literValue,
                        fat = fatValue,
                        snf = snfValue,
                        rate = rateValue,
                        amount = amount
                    )

                    liters = ""
                    fat = ""
                    snf = ""
                    rate = "35"
                    message = "Milk slip saved for $selectedCow"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E963C)
                )
            ) {
                Text("Save Milk Slip", fontSize = 18.sp)
            }

            Text(
                text = if (message.isEmpty()) {
                    "Data saved locally and Firebase."
                } else {
                    message
                },
                color = if (message.isEmpty()) Color.Gray else Color(0xFF0B7A25),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun MilkFieldCard(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        label = { Text(title) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

fun saveMilkSlipLocal(
    context: Context,
    date: String,
    shift: String,
    cowName: String,
    liters: Double,
    fat: Double,
    snf: Double,
    rate: Double,
    amount: Double
) {
    val prefs = context.getSharedPreferences("milk_entries", Context.MODE_PRIVATE)
    val oldJson = prefs.getString("milk_list", "[]") ?: "[]"
    val array = JSONArray(oldJson)

    val obj = JSONObject()
    obj.put("date", date)
    obj.put("shift", shift)
    obj.put("cowName", cowName)
    obj.put("liters", liters)
    obj.put("fat", fat)
    obj.put("snf", snf)
    obj.put("rate", rate)
    obj.put("amount", amount)

    array.put(obj)

    prefs.edit()
        .putString("milk_list", array.toString())
        .apply()
}