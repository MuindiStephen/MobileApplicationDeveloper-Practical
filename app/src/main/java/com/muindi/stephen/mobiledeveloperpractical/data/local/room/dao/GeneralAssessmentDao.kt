package com.muindi.stephen.mobiledeveloperpractical.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.visits.VisitsGeneralAssessmentRequest

@Dao
interface GeneralAssessmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeneralAssessmentInformation(
        visitsGeneralAssessmentRequest: VisitsGeneralAssessmentRequest
    )
}