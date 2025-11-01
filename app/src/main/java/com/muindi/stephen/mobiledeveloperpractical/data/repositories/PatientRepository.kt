package com.muindi.stephen.mobiledeveloperpractical.data.repositories

import com.muindi.stephen.mobiledeveloperpractical.data.local.room.AppLocalDatabase
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.auth.SignInRequest
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.auth.SignUpRequest
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.patients.PatientRegistrationRequest
import com.muindi.stephen.mobiledeveloperpractical.data.remote.PatientsApiService
import com.muindi.stephen.mobiledeveloperpractical.utils.apiRequestByResource
import javax.inject.Inject

class PatientRepository @Inject constructor(
    private val apiService: PatientsApiService,
    appLocalDatabase: AppLocalDatabase
) {
    private val patientRegistrationDao = appLocalDatabase.patientRegistrationDao()


    /**
     * Auth
     */
    suspend fun signUp(signUpRequest: SignUpRequest) = apiRequestByResource {
        apiService.signUpUser(signUpRequest)
    }

    suspend fun signIn(signInRequest: SignInRequest) = apiRequestByResource {
        apiService.signInUser(signInRequest)
    }

    /**
     * Patients
     *
     */

    // Registering a new patient
    suspend fun registerNewPatientRemote(accessToken: String, patientRegistrationRequest: PatientRegistrationRequest ) = apiRequestByResource {
        apiService.registerNewPatient(accessToken = accessToken, patientRegistrationRequest = patientRegistrationRequest)
    }

    suspend fun registerNewPatientLocally(patientRegistrationRequest: PatientRegistrationRequest) = apiRequestByResource {
        patientRegistrationDao.insertPatient(patientRegistrationRequest)
    }



    /**
     * Vitals
     */

    /**
     * Visits
     */

}