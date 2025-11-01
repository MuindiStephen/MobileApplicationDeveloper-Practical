package com.muindi.stephen.mobiledeveloperpractical.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.auth.SignInApiGeneralResponse
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.auth.SignInData
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.auth.SignUpApiGeneralResponse
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.auth.SignUpData
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.auth.SignInRequest
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.auth.SignUpRequest
import com.muindi.stephen.mobiledeveloperpractical.data.repositories.PatientRepository
import com.muindi.stephen.mobiledeveloperpractical.utils.ResourceNetwork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PatientRepository
) : ViewModel() {



    private val _signUpState = MutableStateFlow<ResourceNetwork<SignUpApiGeneralResponse<SignUpData>>>(ResourceNetwork.Loading)
    val signUpState: StateFlow<ResourceNetwork<SignUpApiGeneralResponse<SignUpData>>> = _signUpState

    fun signUpUser(email: String, firstname: String, lastname: String, password: String) = viewModelScope.launch {
        _signUpState.value = repository.signUp(SignUpRequest(
            email = email,
            firstName = firstname,
            lastName = lastname,
            password = password)
        )
    }

    private val _signInState = MutableStateFlow<ResourceNetwork<SignInApiGeneralResponse<SignInData>>>(ResourceNetwork.Loading)
    val signInState: StateFlow<ResourceNetwork<SignInApiGeneralResponse<SignInData>>> = _signInState

    fun signInUser(email: String, password: String) = viewModelScope.launch {
        _signInState.value = repository.signIn(SignInRequest(
            email = email,
            password = password)
        )
    }
}