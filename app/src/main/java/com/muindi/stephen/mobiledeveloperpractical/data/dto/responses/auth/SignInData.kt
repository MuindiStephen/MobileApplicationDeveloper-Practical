package com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.auth

data class SignInData(
    val id: Int,
    val name: String,
    val email: String,
    val updated_at: String,
    val created_at: String,
    val access_token: String
)
