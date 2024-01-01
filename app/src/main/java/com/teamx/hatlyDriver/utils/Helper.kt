package com.teamx.hatlyDriver.utils

import okhttp3.Credentials

object Helper {


    var credentials = Credentials.basic("email@test.com", "test"); //Some initial data

    fun setCredentials(credentials: Credentials) {
        this.credentials = credentials.toString()
    }


    fun getCredential(): String {
        return credentials
    }

    fun isUrl(input: String): Boolean {
        val urlRegex = """^(https?|ftp):\/\/[^\s/$.?#].[^\s]*$"""
        return input.matches(urlRegex.toRegex())
    }
}