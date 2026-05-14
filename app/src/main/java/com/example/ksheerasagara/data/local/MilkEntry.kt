package com.example.ksheerasagara.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "milk_entries")
data class MilkEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val date: String,
    val liters: Double,
    val fat: Double,
    val rate: Double,
    val cowName: String
)