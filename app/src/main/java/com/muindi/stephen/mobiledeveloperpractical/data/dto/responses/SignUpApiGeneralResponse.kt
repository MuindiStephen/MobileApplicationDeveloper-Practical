package com.muindi.stephen.mobiledeveloperpractical.data.dto.responses

data class SignUpApiGeneralResponse<T>(
    val message: String?,
    val success: Boolean? = null,
    val code: Int? = null,
    val data: T? = null,
    val errors: Map<String, List<String>>? = null
)