package com.teamx.hatly.data.remote

import com.google.gson.JsonObject
import com.teamx.hatly.constants.NetworkCallPoints
import com.teamx.hatly.data.dataclasses.ResetPass.ResetPassPhoneData
import com.teamx.hatly.data.dataclasses.forgotPass.forgotPassPhoneData
import com.teamx.hatly.data.dataclasses.login.LoginData
import com.teamx.hatly.data.models.SignUp.RegisterData
import com.teamx.hatly.data.models.otpVerify.OtpVerifyData
import com.teamx.hatly.data.models.otpVerifyForgot.OtpVerifyForgotData
import com.teamx.hatly.data.models.resendOtp.ResendOtpData
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    //Get Post Update Delete

    @POST(NetworkCallPoints.LOGIN_PHONE)
    suspend fun loginPhone(@Body params: JsonObject?): Response<LoginData>

    @POST(NetworkCallPoints.SIGN_UP)
    suspend fun signup(@Body params: JsonObject?): Response<RegisterData>

    @POST(NetworkCallPoints.OTP_VERIFY)
    suspend fun otpVerify(@Body params: JsonObject?): Response<OtpVerifyData>

    @POST(NetworkCallPoints.OTP_VERIFY_PHONE)
    suspend fun otpVerifyForgot(@Body params: JsonObject?): Response<OtpVerifyForgotData>

    @POST(NetworkCallPoints.RESEND_OTP_VERIFY)
    suspend fun resendOtp(@Body params: JsonObject?): Response<ResendOtpData>

    @POST(NetworkCallPoints.FORGOT_PASS_PHONE)
    suspend fun forgotPassPhone(@Body params: JsonObject?): Response<forgotPassPhoneData>

    @POST(NetworkCallPoints.RESET_PASS_PHONE)
    suspend fun resetPassPhone(@Body params: JsonObject?): Response<ResetPassPhoneData>

}