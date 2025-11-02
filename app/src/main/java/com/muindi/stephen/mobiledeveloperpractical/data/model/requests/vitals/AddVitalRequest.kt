package com.muindi.stephen.mobiledeveloperpractical.data.model.requests.vitals

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vital")
data class AddVitalRequest(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bmi: String,
    val height: String,
    val patient_id: String,
    val visit_date: String,
    val weight: String
)