package com.muindi.stephen.mobiledeveloperpractical.utils

import android.content.Context
import android.util.Patterns
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException


sealed class ResourceNetwork<out T> {
    data class Success<out T>(
        val value: T
    ) : ResourceNetwork<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?,
        val errorString: String?
    ) : ResourceNetwork<Nothing>()

    object Loading : ResourceNetwork<Nothing>()
}


/**
 * Handle safe api calling
 */
suspend fun <T> apiRequestByResource(api: suspend () -> T): ResourceNetwork<T> {
    return withContext(Dispatchers.IO) {
        try {
            ResourceNetwork.Success(api.invoke())

        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                val error = throwable.response()?.errorBody()!!.string()
                val message = StringBuilder()

                error.let {
                    try {
                        message.append(JSONObject(it).getString("error_description"))
                    } catch (_: JSONException) {
                    }
                    message.append("\n")
                }

                ResourceNetwork.Failure(
                    false,
                    throwable.code(),
                    throwable.response()?.errorBody(),
                    error
                )
            } else {
                ResourceNetwork.Failure(true, null, null, "NO NETWORK FOUND")
            }
        }
    }
}

fun Fragment.displaySnackBar(text: String) {
    Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT)
        .show()
}

fun saveToken(context: Context, token: String) {
    val sharedPref = context.getSharedPreferences("PatientCareBasePrefs", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString("access_token", token)
        apply()
    }
}

fun getToken(context: Context): String? {
    val sharedPref = context.getSharedPreferences("PatientCareBasePrefs", Context.MODE_PRIVATE)
    return sharedPref.getString("access_token", null)
}


fun isValidEmail(emailAddress: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()
}

fun isValidPassword(password: String): Boolean {
    val hasUpperCase = password.any { it.isUpperCase() }
    val hasLowerCase = password.any { it.isLowerCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecialChar = password.any { !it.isLetterOrDigit() }
    val isLongEnough = password.length >= 8

    return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar && isLongEnough
}



