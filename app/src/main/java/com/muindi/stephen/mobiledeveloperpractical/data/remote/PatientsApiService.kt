package com.muindi.stephen.mobiledeveloperpractical.data.remote

import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.SignUpRequest
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.SignUpApiGeneralResponse
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.SignUpData
import retrofit2.http.Body
import retrofit2.http.POST

interface PatientsApiService {

    @POST("user/signup")
    suspend fun signUpUser(
        @Body signUpRequest: SignUpRequest
    ) : SignUpApiGeneralResponse<SignUpData>

    @POST(REGISTER_END_POINT)
    suspend fun registerUserWithEmail(
        @Body emailSignUpRequest: EmailSignUpRequest
    ) : EmailSignUpResponse

    @POST(REGISTER_END_POINT)
    suspend fun registerUserWithEmail(
        @Body emailSignUpRequest: EmailSignUpRequest
    ) : EmailSignUpResponse

    @POST(REGISTER_END_POINT)
    suspend fun registerUserWithEmail(
        @Body emailSignUpRequest: EmailSignUpRequest
    ) : EmailSignUpResponse

    @POST(REGISTER_END_POINT)
    suspend fun registerUserWithEmail(
        @Body emailSignUpRequest: EmailSignUpRequest
    ) : EmailSignUpResponse

    @POST(REGISTER_END_POINT)
    suspend fun registerUserWithEmail(
        @Body emailSignUpRequest: EmailSignUpRequest
    ) : EmailSignUpResponse

    @POST(REGISTER_END_POINT)
    suspend fun registerUserWithEmail(
        @Body emailSignUpRequest: EmailSignUpRequest
    ) : EmailSignUpResponse

    @POST(REGISTER_END_POINT)
    suspend fun registerUserWithEmail(
        @Body emailSignUpRequest: EmailSignUpRequest
    ) : EmailSignUpResponse

}