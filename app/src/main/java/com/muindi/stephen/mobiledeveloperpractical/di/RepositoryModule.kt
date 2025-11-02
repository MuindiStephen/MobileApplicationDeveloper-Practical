package com.muindi.stephen.mobiledeveloperpractical.di

import com.muindi.stephen.mobiledeveloperpractical.data.local.room.AppLocalDatabase
import com.muindi.stephen.mobiledeveloperpractical.data.local.room.dao.GeneralAssessmentDao
import com.muindi.stephen.mobiledeveloperpractical.data.local.room.dao.OverweightAssessmentDao
import com.muindi.stephen.mobiledeveloperpractical.data.local.room.dao.PatientRegistrationDao
import com.muindi.stephen.mobiledeveloperpractical.data.local.room.dao.VitalDao
import com.muindi.stephen.mobiledeveloperpractical.data.remote.PatientsApiService
import com.muindi.stephen.mobiledeveloperpractical.data.repositories.PatientRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


/**
 * Repository module - di
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun providesRepository(
        apiService: PatientsApiService,
        appLocalDatabase: AppLocalDatabase
    ): PatientRepository {
        return PatientRepository(apiService, appLocalDatabase)
    }

    @Singleton
    @Provides
    fun providesDispatcher() = Dispatchers.Main as CoroutineDispatcher

    @Singleton
    @Provides
    fun providesVitalDao(appDatabase: AppLocalDatabase): VitalDao {
        return appDatabase.vitalDao()
    }

    @Singleton
    @Provides
    fun providesPatientRegistrationDao(appDatabase: AppLocalDatabase): PatientRegistrationDao {
        return appDatabase.patientRegistrationDao()
    }

    @Singleton
    @Provides
    fun providesGeneralAssessmentDao(appDatabase: AppLocalDatabase): GeneralAssessmentDao {
        return appDatabase.visitsGeneralAssessmentInformationDao()
    }

    @Singleton
    @Provides
    fun providesOverweightAssessmentDao(appDatabase: AppLocalDatabase): OverweightAssessmentDao {
        return appDatabase.visitsOverweightAssessmentInformationDao()
    }
}