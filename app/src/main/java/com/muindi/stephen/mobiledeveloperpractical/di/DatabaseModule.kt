package com.muindi.stephen.mobiledeveloperpractical.di

import android.app.Application
import androidx.room.Room
import com.muindi.stephen.mobiledeveloperpractical.data.local.room.AppLocalDatabase
import com.muindi.stephen.mobiledeveloperpractical.data.local.room.dao.PatientRegistrationDao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Database module - di
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesRoomDatabase(context: Application): AppLocalDatabase {
        return Room.databaseBuilder(context, AppLocalDatabase::class.java, "patients.db")
            .allowMainThreadQueries()  // without blocking the main thread
            .fallbackToDestructiveMigration() //  Want database to not be cleared when upgrading versions from 1_2
            .build()
    }

    @Provides
    @Singleton
    fun providePatientRegistrationDao(appLocalDatabase: AppLocalDatabase): PatientRegistrationDao {
        return appLocalDatabase.patientRegistrationDao()
    }

}