package com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.patients

data class RegisteredPatientsResponse(
    val code: Int,
    val `data`: List<RegisterPatientData>,
    val message: String,
    val success: Boolean
)