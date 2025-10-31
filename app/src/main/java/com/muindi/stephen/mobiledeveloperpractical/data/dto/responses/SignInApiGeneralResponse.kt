package com.muindi.stephen.mobiledeveloperpractical.data.dto.responses

data class SignInApiGeneralResponse<T>(
    val message: String?,
    val success: Boolean?,
    val code: Int?,
    val data: T?
)
