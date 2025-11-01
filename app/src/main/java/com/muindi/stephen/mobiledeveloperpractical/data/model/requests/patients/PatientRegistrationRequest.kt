package com.muindi.stephen.mobiledeveloperpractical.data.model.requests.patients

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patient_registration")
data class PatientRegistrationRequest (

    val dob: String,
    val firstname: String,
    val gender: String,
    val lastname: String,
    val reg_date: String,

    @PrimaryKey val unique: String
)