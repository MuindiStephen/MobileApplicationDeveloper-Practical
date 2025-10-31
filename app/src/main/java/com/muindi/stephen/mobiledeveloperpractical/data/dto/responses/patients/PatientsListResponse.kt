package com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.patients

data class PatientsListResponse(
    val code: Int,
    val `data`: List<DataX>,
    val message: String,
    val success: Boolean
)