package com.example.ksheerasagara.data.local

import androidx.room.*

@Dao
interface MilkDao {

    @Insert
    suspend fun insertMilk(entry: MilkEntity)

    @Query("SELECT * FROM milk_entries")
    suspend fun getAllMilk(): List<MilkEntity>
}