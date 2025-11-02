package com.muindi.stephen.mobiledeveloperpractical.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.auth.SignInApiGeneralResponse
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.auth.SignInData
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.auth.SignUpApiGeneralResponse
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.auth.SignUpData
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.patients.PatientRegistrationResponse
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.patients.RegisteredPatientsResponse
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.visits.VisitsResponse
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.vitals.AddVitalResponse
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.auth.SignInRequest
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.auth.SignUpRequest
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.patients.PatientRegistrationRequest
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.visits.VisitsGeneralAssessmentRequest
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.visits.VisitsOverweightAssessmentRequest
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.vitals.AddVitalRequest
import com.muindi.stephen.mobiledeveloperpractical.data.repositories.PatientRepository
import com.muindi.stephen.mobiledeveloperpractical.utils.ResourceNetwork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PatientRepository
) : ViewModel() {



    //user signup
    private val _signUpState = MutableStateFlow<ResourceNetwork<SignUpApiGeneralResponse<SignUpData>>?>(null)
    val signUpState: StateFlow<ResourceNetwork<SignUpApiGeneralResponse<SignUpData>>?> = _signUpState

    fun signUpUser(email: String, firstname: String, lastname: String, password: String) = viewModelScope.launch {

        _signUpState.value = ResourceNetwork.Loading

        _signUpState.value = repository.signUp(SignUpRequest(
            email = email,
            firstName = firstname,
            lastName = lastname,
            password = password)
        )
    }

    //user signin
    private val _signInState = MutableStateFlow<ResourceNetwork<SignInApiGeneralResponse<SignInData>>?>(null)
    val signInState: StateFlow<ResourceNetwork<SignInApiGeneralResponse<SignInData>>?> = _signInState

    fun signInUser(email: String, password: String) = viewModelScope.launch {
        _signInState.value = ResourceNetwork.Loading

        _signInState.value = repository.signIn(SignInRequest(
            email = email,
            password = password)
        )
    }

    //registering a new patient in local storage

    fun addANewPatientLocally(patientRegistrationRequest: PatientRegistrationRequest) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.registerNewPatientLocally(patientRegistrationRequest = patientRegistrationRequest)
        }
    }

    // registering a new patient remotely/online
    private var _addNewPatientRemoteState = MutableStateFlow<ResourceNetwork<PatientRegistrationResponse>?>(null)
    val addNewPatientRemoteState: StateFlow<ResourceNetwork<PatientRegistrationResponse>?> get() = _addNewPatientRemoteState.asStateFlow()

    fun addANewPatientRemotely(
        accessToken: String,
        unique:String,
        reg_date: String,
        firstname: String,
        lastname:String,
        dob: String ,
        gender: String
    ) = viewModelScope.launch {
        _addNewPatientRemoteState.value = ResourceNetwork.Loading

        _addNewPatientRemoteState.value = repository.registerNewPatientRemote(
           accessToken = accessToken,
            patientRegistrationRequest = PatientRegistrationRequest(
                unique = unique,
                reg_date = reg_date,
                firstname = firstname,
                lastname = lastname,
                gender = gender,
                dob = dob
            )
        )
    }

    // vitals
    fun addANewVitalLocally(vitalRequest: AddVitalRequest) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.addVitalLocally(addVitalRequest = vitalRequest)
        }
    }


    // registering a new patient remotely/online
    private var _addNewVitalRemoteState = MutableStateFlow<ResourceNetwork<AddVitalResponse>?>(null)
    val addNewVitalRemoteState: StateFlow<ResourceNetwork<AddVitalResponse>?> get() = _addNewVitalRemoteState.asStateFlow()

    fun addNewVitalRemotely(
        accessToken: String,
        bmi :String,
        height: String,
        patient_id: String,
        visit_date:String,
        weight: String
    ) = viewModelScope.launch {

        _addNewVitalRemoteState.value = ResourceNetwork.Loading


        _addNewVitalRemoteState.value = repository.addVitalRemotely(
            accessToken = accessToken,
            addVitalRequest = AddVitalRequest(
                bmi = bmi,
                height = height,
                patient_id = patient_id,
                visit_date = visit_date,
                weight = weight,
            )
        )
    }

    // General assessment info
    fun addANewGeneralAssessmentInfoLocally(visitsGeneralAssessmentRequest: VisitsGeneralAssessmentRequest) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.addGeneralAssessmentInfoLocally(visitsGeneralAssessmentRequest = visitsGeneralAssessmentRequest)
        }
    }


    private var _addNewGeneralAssessmentRemoteState = MutableStateFlow<ResourceNetwork<VisitsResponse>?>(null)
    val addNewGeneralAssessmentRemoteState: StateFlow<ResourceNetwork<VisitsResponse>?>
        get() = _addNewGeneralAssessmentRemoteState.asStateFlow()

    fun addANewGeneralAssessmentInfoRemotely(
        accessToken: String,
        comments :String,
        general_health: String,
        on_diet: String,
        patient_id: String,
        visit_date: String,
        vital_id : String,
        on_drugs: String
    ) = viewModelScope.launch {

        _addNewGeneralAssessmentRemoteState.value = ResourceNetwork.Loading

        _addNewGeneralAssessmentRemoteState.value = repository.addGeneralAssessmentInfoRemotely(
            accessToken = accessToken,
            visitsGeneralAssessmentRequest = VisitsGeneralAssessmentRequest(
                comments = comments,
                general_health = general_health,
                on_diet = on_diet,
                patient_id = patient_id,
                visit_date = visit_date,
                vital_id = vital_id,
                on_drugs = on_drugs
            )
        )
    }


    // overweight assessment info
    fun addANewOverweightAssessmentInfoLocally(visitsOverweightAssessmentRequest: VisitsOverweightAssessmentRequest) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.addOverweightAssessmentInfoLocally(visitsOverweightAssessmentRequest = visitsOverweightAssessmentRequest)
        }
    }


    private var _addNewOverweightAssessmentRemoteState = MutableStateFlow<ResourceNetwork<VisitsResponse>?>(null)
    val addNewOverweightAssessmentRemoteState: StateFlow<ResourceNetwork<VisitsResponse>?>
        get() = _addNewOverweightAssessmentRemoteState.asStateFlow()

    fun addANewOverweightAssessmentInfoRemotely(
        accessToken: String,
        comments :String,
        general_health: String,
        on_diet: String,
        patient_id: String,
        visit_date: String,
        vital_id : String,
        on_drugs: String
    ) = viewModelScope.launch {

        _addNewOverweightAssessmentRemoteState.value = ResourceNetwork.Loading

        _addNewOverweightAssessmentRemoteState.value = repository.addOverweightAssessmentInfoRemotely(
            accessToken = accessToken,
            visitsOverweightAssessmentRequest = VisitsOverweightAssessmentRequest(
                comments = comments,
                general_health = general_health,
                on_diet = on_diet,
                patient_id = patient_id,
                visit_date = visit_date,
                vital_id = vital_id,
                on_drugs = on_drugs
            )
        )
    }

    fun fetchAllPatientListing(accessToken: String) : LiveData<RegisteredPatientsResponse> {
        return repository.fetchAllPatientListing(accessToken = accessToken)
    }

    fun resetSignUpState() {
        _signUpState.value = null
    }

    fun resetSignInState() {
        _signInState.value = null
    }



    fun resetPatientRegistrationState() {
       _addNewPatientRemoteState.value = null
    }

    fun resetVitalState() {
        _addNewVitalRemoteState.value = null
    }

    fun resetGeneralAssessState() {
        _addNewGeneralAssessmentRemoteState.value = null
    }

    fun resetOverWeightAssessState() {
        _addNewOverweightAssessmentRemoteState.value = null
    }
}