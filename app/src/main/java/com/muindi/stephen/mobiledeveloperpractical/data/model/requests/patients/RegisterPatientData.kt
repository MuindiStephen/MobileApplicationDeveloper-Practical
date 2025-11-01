package com.muindi.stephen.mobiledeveloperpractical.data.model.requests.patients

data class RegisterPatientData(
    val created_at: String,
    val dob: String,
    val firstname: String,
    val gender: String,
    val id: Int,
    val lastname: String,
    val reg_date: String,
    val unique: String,
    val updated_at: String,
    val user_id: Int
)