package com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.patients

data class AddPatientResponse(
    val code: Int,
    val `data`: Data,
    val message: String,
    val success: Boolean
)