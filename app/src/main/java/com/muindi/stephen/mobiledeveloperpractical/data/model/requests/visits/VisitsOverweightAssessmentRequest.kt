package com.muindi.stephen.mobiledeveloperpractical.data.model.requests.visits

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "overweight_assessment_information")
data class VisitsOverweightAssessmentRequest(
    val comments: String,
    val general_health: String,
    val on_diet: String,
    val on_drugs: String,
    @PrimaryKey val patient_id: String,
    val visit_date: String,
    val vital_id: String
)