package com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.patients

data class ViewPatientResponse(
    val code: Int,
    val `data`: List<DataXX>,
    val message: String,
    val success: Boolean
)