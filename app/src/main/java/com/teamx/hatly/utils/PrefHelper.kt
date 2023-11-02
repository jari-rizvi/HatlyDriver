package com.teamx.hatly.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.teamx.hatly.data.dataclasses.login.LoginData

class PrefHelper private constructor() {

    companion object {
        private val sharePref = PrefHelper()
        private lateinit var sharedPreferences: SharedPreferences
        private const val USER_DATA = "USERDATA"



        fun getInstance(context: Context): PrefHelper {
            if (!::sharedPreferences.isInitialized) {
                synchronized(PrefHelper::class.java) {
                    if (!::sharedPreferences.isInitialized) {
                        sharedPreferences =
                            context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
                    }
                }
            }
            return sharePref
        }
    }


    fun getUserData(): LoginData? {
        val gson = Gson()
        return gson.fromJson(
            sharedPreferences.getString(USER_DATA, ""), LoginData::class.java
        )
    }

    fun setUserData(shippingAddress: LoginData?) {
        val gson = Gson()
        val str = gson.toJson(shippingAddress)
        sharedPreferences.edit().putString(USER_DATA, str).apply()
    }

}