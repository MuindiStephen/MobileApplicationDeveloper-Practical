package com.muindi.stephen.mobiledeveloperpractical.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.patients.PatientRegistrationRequest

@Dao
interface PatientRegistrationDao {
    @Insert
    suspend fun insertPatient(patient: PatientRegistrationRequest)

}