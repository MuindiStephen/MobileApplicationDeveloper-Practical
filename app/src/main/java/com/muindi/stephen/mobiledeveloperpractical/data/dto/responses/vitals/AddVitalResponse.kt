package com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.vitals

data class AddVitalResponse(
    val code: Int,
    val `data`: Data,
    val message: String,
    val success: Boolean
)