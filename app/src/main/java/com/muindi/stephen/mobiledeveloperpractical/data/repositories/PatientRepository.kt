package com.muindi.stephen.mobiledeveloperpractical.data.repositories

import com.muindi.stephen.mobiledeveloperpractical.data.local.room.AppLocalDatabase
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.patients.PatientRegistrationRequest
import com.muindi.stephen.mobiledeveloperpractical.data.remote.PatientsApiService
import com.muindi.stephen.mobiledeveloperpractical.utils.apiRequestByResource
import javax.inject.Inject

class PatientRepository @Inject constructor(
    private val apiService: PatientsApiService,
    private val appLocalDatabase: AppLocalDatabase
) {
    private val patientRegistrationDao = appLocalDatabase.patientRegistrationDao()


    suspend fun registerPatient(patientRegistrationRequest: PatientRegistrationRequest ) = apiRequestByResource {
        apiService.signUpUser(patientRegistrationRequest)
    }
}