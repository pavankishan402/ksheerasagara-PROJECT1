package com.example.ksheerasagara.ui.screens

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ksheerasagara.data.ExpenseData
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddMilkClick: () -> Unit,
    onDailyPassbookClick: () -> Unit,
    onMonthlyPassbookClick: () -> Unit,
    onExpenseClick: () -> Unit,
    onProfitClick: () -> Unit,
    onAISuggestionClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val milkStats = remember { getMilkStats(context) }

    val expenses = remember {
        try {
            ExpenseData.getAllExpenses(context)
        } catch (e: Exception) {
            emptyList()
        }
    }

    val totalExpense = expenses.sumOf { it.amount }
    val fodder = expenses.filter { it.category == "Fodder" }.sumOf { it.amount }
    val medical = expenses.filter { it.category == "Medical" }.sumOf { it.amount }
    val labor = expenses.filter { it.category == "Labor" }.sumOf { it.amount }
    val other = expenses.filter { it.category == "Other" }.sumOf { it.amount }

    val profit = milkStats.income - totalExpense
    val profitPerLiter =
        if (milkStats.liters > 0) profit / milkStats.liters else 0.0

    val monthText = remember {
        SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Ksheera Sagara",
                    modifier = Modifier.padding(18.dp),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                NavigationDrawerItem(
                    label = { Text("Milk Slip") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onAddMilkClick()
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Daily Passbook") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onDailyPassbookClick()
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Monthly Passbook") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onMonthlyPassbookClick()
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Expenses") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onExpenseClick()
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Cows") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onProfitClick()
                    }
                )

                NavigationDrawerItem(
                    label = { Text("AI Suggestions") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onAISuggestionClick()
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Logout") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onLogoutClick()
                    }
                )
            }
        }
    ) {
        Scaffold(
            containerColor = Color(0xFFF1F8EA),
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(118.dp)
                        .background(Color(0xFF145F1F))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Ksheera-Sagara",
                                color = Color.White,
                                fontSize = 13.sp
                            )
                            Text(
                                text = monthText,
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IconButton(onClick = onLogoutClick) {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAddMilkClick,
                    containerColor = Color(0xFF2E963C),
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, null, tint = Color.White)
                }
            },
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(
                        selected = true,
                        onClick = {},
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
                        selected = false,
                        onClick = onProfitClick,
                        icon = { Icon(Icons.Default.Pets, null) },
                        label = { Text("Cows") }
                    )

                    NavigationBarItem(
                        selected = false,
                        onClick = onAISuggestionClick,
                        icon = { Icon(Icons.Default.Schedule, null) },
                        label = { Text("AI") }
                    )
                }
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (profit >= 0) Color(0xFF2E963C)
                            else Color(0xFFD62828)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Net Profit / Loss", color = Color.White)
                            Text(
                                text = "₹ %.0f".format(profit),
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${milkStats.entries + expenses.size} entries this month",
                                color = Color.White
                            )
                        }

                        Surface(
                            color = Color.White.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Text(
                                text = if (profit >= 0) "Profit" else "Loss",
                                color = Color.White,
                                modifier = Modifier.padding(
                                    horizontal = 14.dp,
                                    vertical = 6.dp
                                )
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SmallDashboardCard(
                        title = "Total Income",
                        value = "₹ %.0f".format(milkStats.income),
                        subtitle = "%.1f liters sold".format(milkStats.liters),
                        valueColor = Color(0xFF0B7A25),
                        modifier = Modifier.weight(1f)
                    )

                    SmallDashboardCard(
                        title = "Total Expense",
                        value = "₹ %.0f".format(totalExpense),
                        subtitle = "${expenses.map { it.category }.distinct().size} categories",
                        valueColor = Color.Red,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SmallDashboardCard(
                        title = "Avg Fat%",
                        value = "%.1f%%".format(milkStats.avgFat),
                        subtitle = "Good quality",
                        valueColor = Color.Black,
                        modifier = Modifier.weight(1f)
                    )

                    SmallDashboardCard(
                        title = "Profit / Liter",
                        value = "₹ %.2f".format(profitPerLiter),
                        subtitle = "This month",
                        valueColor = if (profitPerLiter >= 0) Color(0xFF0B7A25) else Color.Red,
                        modifier = Modifier.weight(1f)
                    )
                }

                Text(
                    text = "EXPENSE BREAKDOWN",
                    fontSize = 17.sp,
                    color = Color(0xFF37474F)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ExpenseDonutChart(
                            fodder = fodder,
                            medical = medical,
                            labor = labor,
                            other = other
                        )

                        Spacer(modifier = Modifier.width(18.dp))

                        Column {
                            LegendRow("Fodder", fodder, totalExpense, Color(0xFF2E963C))
                            LegendRow("Medical", medical, totalExpense, Color(0xFFFF7F0E))
                            LegendRow("Labor", labor, totalExpense, Color(0xFF1976D2))
                            LegendRow("Other", other, totalExpense, Color.Gray)
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAISuggestionClick() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "🌸 AI Tip of the Day",
                            color = Color(0xFF2E963C),
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text =
                                if (profit < 0)
                                    "Your expenses are higher than income. Reduce fodder or medical cost to improve profit."
                                else
                                    "Your dairy is in profit. Continue tracking cow-wise milk and expenses daily."
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SmallDashboardCard(
    title: String,
    value: String,
    subtitle: String,
    valueColor: Color,
    modifier: Modifier
) {
    Card(
        modifier = modifier.height(90.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontSize = 13.sp)
            Text(
                value,
                fontSize = 22.sp,
                color = valueColor,
                fontWeight = FontWeight.Bold
            )
            Text(subtitle, fontSize = 12.sp)
        }
    }
}

@Composable
fun ExpenseDonutChart(
    fodder: Double,
    medical: Double,
    labor: Double,
    other: Double
) {
    val total = fodder + medical + labor + other

    Canvas(modifier = Modifier.size(95.dp)) {
        if (total <= 0) {
            drawArc(
                color = Color.LightGray,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(20f, cap = StrokeCap.Butt)
            )
        } else {
            var start = -90f

            listOf(
                fodder to Color(0xFF2E963C),
                medical to Color(0xFFFF7F0E),
                labor to Color(0xFF1976D2),
                other to Color.Gray
            ).forEach { item ->
                val sweep = ((item.first / total) * 360).toFloat()

                drawArc(
                    color = item.second,
                    startAngle = start,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(20f, cap = StrokeCap.Butt)
                )

                start += sweep
            }
        }
    }
}

@Composable
fun LegendRow(
    title: String,
    value: Double,
    total: Double,
    color: Color
) {
    val percent =
        if (total > 0) (value / total * 100).toInt() else 0

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text("$title — $percent%")
    }
}

data class MilkStats(
    val liters: Double,
    val income: Double,
    val avgFat: Double,
    val entries: Int
)

fun getMilkStats(context: Context): MilkStats {
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
            val amount = obj.optDouble("amount", 0.0)

            liters += l
            income += amount

            if (f > 0) {
                fatTotal += f
                fatCount++
            }
        }

        MilkStats(
            liters = liters,
            income = income,
            avgFat = if (fatCount > 0) fatTotal / fatCount else 0.0,
            entries = array.length()
        )

    } catch (e: Exception) {
        MilkStats(0.0, 0.0, 0.0, 0)
    }
}