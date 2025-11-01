package com.muindi.stephen.mobiledeveloperpractical.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper  (context: Context) {

    private val PATIENTAPP = "appsharedpreferences"
    private val spUserID = "spUserID"

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PATIENTAPP, Context.MODE_PRIVATE)
            ?: throw IllegalStateException("Context is null")

    private val spEditor = sharedPreferences.edit()

    fun setIfFirstTimeLogin(activity: Context, dataJson: Boolean) {
        val sharedPref = activity.getSharedPreferences(
            "firstTimeLoginUser", Context.MODE_PRIVATE
        )

        with(sharedPref.edit()) {
            putBoolean("firstTimeLoginUser", dataJson)
            commit()
        }
    }

    fun getIfFirstTimeLogin(activity: Context): Boolean {
        val sharedPref = activity.getSharedPreferences(
            "firstTimeLoginUser", Context.MODE_PRIVATE
        )
        return sharedPref.getBoolean("firstTimeLoginUser", true)
    }

    fun setIfToShowOnAuthDashboard(activity: Context, dataJson: Boolean) {
        val sharedPref = activity.getSharedPreferences(
            "showAuthDashboard",
            Context.MODE_PRIVATE
        )
        with(sharedPref!!.edit()) {
            putBoolean("showAuthDashboard", dataJson)
            commit()
        }
    }

    fun getIfToShowOnAuthDashboard(activity: Context?): Boolean {
        val sharedPref = activity?.getSharedPreferences(
            "showAuthDashboard",
            Context.MODE_PRIVATE
        )
        val tellerJsonData = sharedPref!!.getBoolean("showAuthDashboard", true)
        return tellerJsonData!!
    }

    fun clearSession() {
        spEditor.clear()
        spEditor.apply()
    }
}