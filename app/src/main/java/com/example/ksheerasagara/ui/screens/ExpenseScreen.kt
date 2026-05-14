package com.example.ksheerasagara.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ksheerasagara.data.Expense
import com.example.ksheerasagara.data.ExpenseData
import com.example.ksheerasagara.firebase.FirestoreManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current

    val cowList = remember {
        getSavedCows(context).map {
            "${it.name} (${it.breed})"
        }
    }

    var selectedCow by remember {
        mutableStateOf(
            cowList.firstOrNull() ?: "No Cow Added"
        )
    }

    val categories = listOf(
        "Fodder",
        "Medical",
        "Labor",
        "Other"
    )

    var selectedCategory by remember {
        mutableStateOf("Fodder")
    }

    var amount by remember {
        mutableStateOf("")
    }

    var message by remember {
        mutableStateOf("")
    }

    var cowExpanded by remember {
        mutableStateOf(false)
    }

    var categoryExpanded by remember {
        mutableStateOf(false)
    }

    Scaffold(

        containerColor = Color(0xFFF1F8EA),

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        "Add Expense",
                        color = Color.White
                    )
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBack
                    ) {

                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
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

            // SELECT COW

            ExposedDropdownMenuBox(
                expanded = cowExpanded,
                onExpandedChange = {
                    cowExpanded = !cowExpanded
                }
            ) {

                OutlinedTextField(
                    value = selectedCow,
                    onValueChange = {},
                    readOnly = true,

                    label = {
                        Text("Select Cow")
                    },

                    trailingIcon = {
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
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
                    onDismissRequest = {
                        cowExpanded = false
                    }
                ) {

                    cowList.forEach { cow ->

                        DropdownMenuItem(
                            text = {
                                Text(cow)
                            },

                            onClick = {
                                selectedCow = cow
                                cowExpanded = false
                            }
                        )
                    }
                }
            }

            // CATEGORY

            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = {
                    categoryExpanded = !categoryExpanded
                }
            ) {

                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,

                    label = {
                        Text("Expense Category")
                    },

                    trailingIcon = {
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
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
                    expanded = categoryExpanded,
                    onDismissRequest = {
                        categoryExpanded = false
                    }
                ) {

                    categories.forEach { category ->

                        DropdownMenuItem(
                            text = {
                                Text(category)
                            },

                            onClick = {
                                selectedCategory = category
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }

            // AMOUNT

            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it
                },

                label = {
                    Text("Amount ₹")
                },

                singleLine = true,

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(16.dp),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            // SAVE BUTTON

            Button(

                onClick = {

                    val amountValue =
                        amount.toDoubleOrNull() ?: 0.0

                    if (selectedCow == "No Cow Added") {

                        message = "Please add cow first"
                        return@Button
                    }

                    if (amountValue <= 0) {

                        message = "Enter valid amount"
                        return@Button
                    }

                    val expense = Expense(

                        cowName = selectedCow,

                        category = selectedCategory,

                        amount = amountValue
                    )

                    // LOCAL SAVE

                    ExpenseData.addExpense(
                        context,
                        expense
                    )

                    // FIREBASE SAVE

                    FirestoreManager.saveExpense(
                        cowName = selectedCow,
                        category = selectedCategory,
                        amount = amountValue
                    )

                    amount = ""

                    message =
                        "Expense saved for $selectedCow"
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),

                shape = RoundedCornerShape(14.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E963C)
                )

            ) {

                Text("Save Expense")
            }

            if (message.isNotEmpty()) {

                Text(
                    text = message,
                    color = Color(0xFF0B7A25)
                )
            }
        }
    }
}