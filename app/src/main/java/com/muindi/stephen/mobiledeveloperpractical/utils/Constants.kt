package com.muindi.stephen.mobiledeveloperpractical.utils

object Constants {

    init {
        System.loadLibrary("native-lib")
    }

    @JvmStatic
    external fun getStringBaseUrlDevelopment(): String

}