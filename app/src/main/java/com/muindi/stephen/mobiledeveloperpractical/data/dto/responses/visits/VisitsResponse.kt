package com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.visits

data class VisitsResponse(
    val code: Int,
    val `data`: Data,
    val message: String,
    val success: Boolean
)