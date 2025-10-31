package com.muindi.stephen.mobiledeveloperpractical.data.model.requests.patients

data class AddPatientRequest(
    val dob: String,
    val firstname: String,
    val gender: String,
    val lastname: String,
    val reg_date: String,
    val unique: String
)