package com.muindi.stephen.mobiledeveloperpractical.di

import com.muindi.stephen.mobiledeveloperpractical.data.local.room.AppLocalDatabase
import com.muindi.stephen.mobiledeveloperpractical.data.remote.PatientsApiService
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
}