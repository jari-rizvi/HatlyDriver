package com.teamx.hatly.data.remote.reporitory

import com.google.gson.JsonObject
import com.teamx.hatly.data.local.db.AppDao
import com.teamx.hatly.data.local.dbModel.CartDao
import com.teamx.hatly.data.remote.ApiService
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService, var localDataSource: AppDao, var localDataSource2: CartDao
) {
    suspend fun loginPhone(@Body param: JsonObject) = apiService.loginPhone(param)

    suspend fun signup(@Body param: JsonObject) = apiService.signup(param)

    suspend fun otpVerify(@Body param: JsonObject) = apiService.otpVerify(param)

    suspend fun otpVerifyForgot(@Body param: JsonObject) = apiService.otpVerifyForgot(param)

    suspend fun resendOtp(@Body param: JsonObject) = apiService.resendOtp(param)

    suspend fun forogtPassPhone(@Body param: JsonObject) = apiService.forgotPassPhone(param)
    suspend fun resetPassPhone(@Body param: JsonObject) = apiService.resetPassPhone(param)

    suspend fun notification() = apiService.notification()


    suspend fun getOrders(
         @Query("requestFor") requestFor: String
    ) = apiService.getOrders(requestFor)

    suspend fun fcm(@Body param: JsonObject) = apiService.fcm(param)



    suspend fun getOrdersByStatus(
        @Query("status") status: String
    ) = apiService.getOrdersByStatus(status)

    suspend fun getActiveOrders(
        @Query("status") status: String,
        @Query("requestFor") requestFor: String
    ) = apiService.getActiveOrders(status, requestFor)


    suspend fun acceptRejectOrder(
        @Path("id") id: String, @Body param: JsonObject
    ) = apiService.acceptRejectOrder(id, param)


}