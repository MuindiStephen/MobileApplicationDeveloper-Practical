package com.muindi.stephen.mobiledeveloperpractical.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.muindi.stephen.mobiledeveloperpractical.data.local.room.dao.PatientRegistrationDao
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.patients.PatientRegistrationRequest


@Database(entities = [PatientRegistrationRequest::class], version = 1, exportSchema = false)
abstract class AppLocalDatabase : RoomDatabase() {

    abstract fun patientRegistrationDao(): PatientRegistrationDao
}