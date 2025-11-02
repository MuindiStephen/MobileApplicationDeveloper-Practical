package com.muindi.stephen.mobiledeveloperpractical.data.remote

import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.auth.SignInApiGeneralResponse
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.auth.SignInData
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.auth.SignUpRequest
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.auth.SignUpApiGeneralResponse
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.auth.SignUpData
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.patients.PatientRegistrationResponse
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.auth.SignInRequest
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.patients.PatientRegistrationRequest
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.patients.RegisterPatientData
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.patients.RegisteredPatientsResponse
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.vitals.AddVitalResponse
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.visits.VisitsGeneralAssessmentRequest
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.visits.VisitsOverweightAssessmentRequest
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.vitals.AddVitalRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface PatientsApiService {

    /**
     * User Auth
     */
    @POST("user/signup")
    suspend fun signUpUser(
        @Body signUpRequest: SignUpRequest
    ) : SignUpApiGeneralResponse<SignUpData>

    @POST("user/login")
    suspend fun signInUser(
        @Body signInRequest: SignInRequest
    ) : SignInApiGeneralResponse<SignInData>

    /**
     * Patients
     */
    @POST("patients/register")
    suspend fun registerNewPatient(
        @Header("Authorization") accessToken: String,
        @Body patientRegistrationRequest: PatientRegistrationRequest
    ) : PatientRegistrationResponse

    @GET("patients/list")
    suspend fun getAllRegisteredPatients(
        @Header("Authorization") accessToken: String,
    ) : RegisteredPatientsResponse

    @GET("patients/show/{id}")
    suspend fun getSpecificPatientInformation(
        @Header("Authorization") accessToken: String,
    ) : RegisterPatientData

    /**
     * Vitals
     */
    @POST("vital/add")
    suspend fun addPatientVitals(
        @Header("Authorization") accessToken: String,
        @Body vitalRequest: AddVitalRequest
    ): AddVitalResponse

    @GET("patients/list")
    suspend fun getAllVisitsForParticularDate(@Header("Authorization") accessToken: String,)

    /**
     * Visits
     */
    @POST("visits/add")
    suspend fun addGeneralAssessmentInformation(
        @Header("Authorization") accessToken: String,
        @Body visitsGeneralAssessmentRequest: VisitsGeneralAssessmentRequest
    )

    @POST("visits/add")
    suspend fun addOverweightAssessmentInformation(
        @Header("Authorization") accessToken: String,
        @Body visitsOverweightAssessmentRequest: VisitsOverweightAssessmentRequest
    )

}