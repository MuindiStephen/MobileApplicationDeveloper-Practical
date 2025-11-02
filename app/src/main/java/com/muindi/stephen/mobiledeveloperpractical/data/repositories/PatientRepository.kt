package com.muindi.stephen.mobiledeveloperpractical.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.patients.RegisteredPatientsResponse
import com.muindi.stephen.mobiledeveloperpractical.data.local.room.AppLocalDatabase
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.auth.SignInRequest
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.auth.SignUpRequest
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.patients.PatientRegistrationRequest
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.visits.VisitsGeneralAssessmentRequest
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.visits.VisitsOverweightAssessmentRequest
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.vitals.AddVitalRequest
import com.muindi.stephen.mobiledeveloperpractical.data.remote.PatientsApiService
import com.muindi.stephen.mobiledeveloperpractical.utils.apiRequestByResource
import timber.log.Timber
import javax.inject.Inject

class PatientRepository @Inject constructor(
    private val apiService: PatientsApiService,
    appLocalDatabase: AppLocalDatabase
) {
    private val patientRegistrationDao = appLocalDatabase.patientRegistrationDao()
    private val vitalsDao = appLocalDatabase.vitalDao()
    private val visitsOverweightAssessmentInformationDao = appLocalDatabase.visitsOverweightAssessmentInformationDao()
    private val visitsGeneralAssessmentInformationDao = appLocalDatabase.visitsGeneralAssessmentInformationDao()


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
    suspend fun addVitalRemotely(accessToken: String, addVitalRequest: AddVitalRequest ) = apiRequestByResource {
        apiService.addPatientVitals(accessToken = accessToken, vitalRequest = addVitalRequest)
    }

    suspend fun addVitalLocally(addVitalRequest: AddVitalRequest) = apiRequestByResource {
        vitalsDao.addVital(addVitalRequest = addVitalRequest)
    }

    /**
     * Visits
     */
    // General Assessment information
    suspend fun addGeneralAssessmentInfoRemotely(accessToken: String, visitsGeneralAssessmentRequest: VisitsGeneralAssessmentRequest ) = apiRequestByResource {
        apiService.addGeneralAssessmentInformation(accessToken = accessToken, visitsGeneralAssessmentRequest = visitsGeneralAssessmentRequest)
    }

    suspend fun addGeneralAssessmentInfoLocally(visitsGeneralAssessmentRequest: VisitsGeneralAssessmentRequest) = apiRequestByResource {
        visitsGeneralAssessmentInformationDao.insertGeneralAssessmentInformation(visitsGeneralAssessmentRequest = visitsGeneralAssessmentRequest)
    }

    // Overweight Assessment information
    suspend fun addOverweightAssessmentInfoRemotely(accessToken: String, visitsOverweightAssessmentRequest: VisitsOverweightAssessmentRequest) = apiRequestByResource {
        apiService.addOverweightAssessmentInformation(accessToken = accessToken, visitsOverweightAssessmentRequest = visitsOverweightAssessmentRequest)
    }

    suspend fun addOverweightAssessmentInfoLocally(visitsOverweightAssessmentRequest: VisitsOverweightAssessmentRequest) = apiRequestByResource {
        visitsOverweightAssessmentInformationDao.insertOverweightAssessmentInformation(visitsOverweightAssessmentRequest)
    }

    /**
     * Patients listing
     */

    fun fetchAllPatientListing(accessToken: String): LiveData<RegisteredPatientsResponse> = liveData {
        try {
            Timber.d("Success: Pulled patients")
            val response = apiService.getAllRegisteredPatients(accessToken = accessToken)
            emit(response) // Emit the response as LiveData
        } catch (e: Exception) {
            Timber.d("::Error:: Could not pull patients :WHY: ${e.message}")
        }
    }
}