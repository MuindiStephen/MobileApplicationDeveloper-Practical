package com.muindi.stephen.mobiledeveloperpractical.utils

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