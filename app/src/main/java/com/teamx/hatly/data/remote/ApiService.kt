package com.teamx.hatly.data.remote

import com.google.gson.JsonObject
import com.teamx.hatly.constants.NetworkCallPoints
import com.teamx.hatly.data.dataclasses.login.LoginData
import com.teamx.hatly.data.models.SignUp.RegisterData
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    //Get Post Update Delete

    @POST(NetworkCallPoints.LOGIN_PHONE)
    suspend fun loginPhone(@Body params: JsonObject?): Response<LoginData>

    @POST(NetworkCallPoints.SIGN_UP)
    suspend fun signup(@Body params: JsonObject?): Response<RegisterData>

}