package com.teamx.hatly.data.remote.reporitory

import com.google.gson.JsonObject
import com.teamx.hatly.data.local.db.AppDao
import com.teamx.hatly.data.local.dbModel.CartDao
import com.teamx.hatly.data.remote.ApiService
import retrofit2.http.Body
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

    suspend fun getOrders(
       /* @Query("limit") limit: String,
        @Query("page") page: String,
        @Query("orderType") orderType: String*/
    ) = apiService.getOrders(/*limit, page, orderType*/)
  suspend fun getOrdersByStatus(
        @Query("status") status: String
    ) = apiService.getOrdersByStatus(status)


}