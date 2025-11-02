package com.muindi.stephen.mobiledeveloperpractical.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.muindi.stephen.mobiledeveloperpractical.data.local.room.dao.PatientRegistrationDao
import com.muindi.stephen.mobiledeveloperpractical.data.local.room.dao.VitalDao
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.patients.PatientRegistrationRequest
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.vitals.AddVitalRequest


@Database(
    entities = [PatientRegistrationRequest::class, AddVitalRequest::class],
    version = 1,
    exportSchema = false
)
abstract class AppLocalDatabase : RoomDatabase() {
    abstract fun patientRegistrationDao(): PatientRegistrationDao
    abstract fun vitalDao(): VitalDao
}