package com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.patients


data class PatientRegistrationResponse(
    val code: Int,
    val `data`: Data,
    val message: String,
    val success: Boolean
)