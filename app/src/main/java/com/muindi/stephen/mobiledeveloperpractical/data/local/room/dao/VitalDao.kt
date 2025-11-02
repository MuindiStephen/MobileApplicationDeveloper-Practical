package com.muindi.stephen.mobiledeveloperpractical.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.vitals.AddVitalRequest

@Dao
interface VitalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVital(addVitalRequest: AddVitalRequest)
}