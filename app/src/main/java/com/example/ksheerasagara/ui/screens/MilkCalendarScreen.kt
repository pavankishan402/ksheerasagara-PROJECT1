package com.example.ksheerasagara.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ksheerasagara.data.MilkCalendarStore
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MilkCalendarScreen() {

    val context = LocalContext.current
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val milkDates by remember {
        mutableStateOf(MilkCalendarStore.getMilkEntryDates(context))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(Color(0xFF607D8B))
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = {
                    currentMonth = currentMonth.minusMonths(1)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Previous",
                    tint = Color.White
                )
            }

            Text(
                text = "${
                    currentMonth.month.getDisplayName(
                        TextStyle.FULL,
                        Locale.ENGLISH
                    )
                } ${currentMonth.year}",
                modifier = Modifier.weight(1f),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            IconButton(
                onClick = {
                    currentMonth = currentMonth.plusMonths(1)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Next",
                    tint = Color.White
                )
            }
        }

        WeekDaysRow()

        CalendarGrid(
            currentMonth = currentMonth,
            selectedDate = selectedDate,
            milkDates = milkDates,
            onDateClick = {
                selectedDate = it
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Selected Date: $selectedDate",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = if (milkDates.contains(selectedDate.toString())) {
                "Milk entry available on this date"
            } else {
                "No milk entry on this date"
            },
            modifier = Modifier.padding(16.dp),
            color = if (milkDates.contains(selectedDate.toString())) {
                Color(0xFF0B8F08)
            } else {
                Color.Gray
            }
        )
    }
}

@Composable
fun WeekDaysRow() {
    val days = listOf("M", "T", "W", "T", "F", "S", "S")

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        days.forEach { day ->
            Text(
                text = day,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp),
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun CalendarGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    milkDates: Set<String>,
    onDateClick: (LocalDate) -> Unit
) {
    val firstDay = currentMonth.atDay(1)
    val totalDays = currentMonth.lengthOfMonth()
    val startOffset = firstDay.dayOfWeek.value - 1

    val previousMonth = currentMonth.minusMonths(1)
    val previousMonthDays = previousMonth.lengthOfMonth()

    Column {
        for (week in 0 until 6) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                for (day in 0 until 7) {

                    val index = week * 7 + day
                    val dayNumber = index - startOffset + 1

                    val date: LocalDate?
                    val text: String
                    val isCurrentMonth: Boolean

                    when {
                        dayNumber < 1 -> {
                            text = (previousMonthDays + dayNumber).toString()
                            date = null
                            isCurrentMonth = false
                        }

                        dayNumber > totalDays -> {
                            text = (dayNumber - totalDays).toString()
                            date = null
                            isCurrentMonth = false
                        }

                        else -> {
                            text = dayNumber.toString()
                            date = currentMonth.atDay(dayNumber)
                            isCurrentMonth = true
                        }
                    }

                    val hasMilkEntry =
                        date != null && milkDates.contains(date.toString())

                    val isSelected =
                        date == selectedDate

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(58.dp)
                            .clickable(enabled = date != null) {
                                date?.let {
                                    onDateClick(it)
                                }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    color = if (isSelected) {
                                        Color(0xFFE3F2FD)
                                    } else {
                                        Color.Transparent
                                    },
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = text,
                                color = when {
                                    !isCurrentMonth -> Color.LightGray
                                    isSelected -> Color(0xFF1976D2)
                                    else -> Color.DarkGray
                                },
                                fontWeight = if (isSelected) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .background(
                                    color = if (hasMilkEntry) {
                                        Color(0xFF0B8F08)
                                    } else {
                                        Color.Transparent
                                    },
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }

            Divider(color = Color(0xFFE0E0E0))
        }
    }
}