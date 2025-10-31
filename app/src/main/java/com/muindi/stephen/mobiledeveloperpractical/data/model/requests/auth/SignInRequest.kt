package com.muindi.stephen.mobiledeveloperpractical.data.model.requests.auth

import com.google.gson.annotations.SerializedName

data class SignInRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
)
