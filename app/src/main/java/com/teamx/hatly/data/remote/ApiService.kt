package com.teamx.hatly.data.remote

import com.google.gson.JsonObject
import com.teamx.hatly.constants.NetworkCallPoints
import com.teamx.hatly.constants.NetworkCallPoints.Companion.TOKENER
import com.teamx.hatly.data.dataclasses.ResetPass.ResetPassPhoneData
import com.teamx.hatly.data.dataclasses.SignUp.RegisterData
import com.teamx.hatly.data.dataclasses.fcmmodel.FcmModel
import com.teamx.hatly.data.dataclasses.forgotPass.ForgotData
import com.teamx.hatly.data.dataclasses.getActiveorder.GetActiveOrderData
import com.teamx.hatly.data.dataclasses.login.LoginData
import com.teamx.hatly.data.dataclasses.meModel.me.MeModel
import com.teamx.hatly.data.dataclasses.model.ModelVerifyPassOtp
import com.teamx.hatly.data.dataclasses.modelUploadImages.ModelUploadImages
import com.teamx.hatly.data.dataclasses.pastParcels.GetPastParcelsData
import com.teamx.hatly.data.dataclasses.pastorder.PastOrdersData
import com.teamx.hatly.data.dataclasses.sucess.SuccessData
import com.teamx.hatly.data.dataclasses.transactionHistory.TransactionHistoryData
import com.teamx.hatly.ui.fragments.Dashboard.notification.modelNotification.ModelNotification
import com.teamx.hatly.ui.fragments.payments.paymentmethod.defaultmodel.ModelDefaultCredCards
import com.teamx.hatly.ui.fragments.payments.paymentmethod.modelDetach.ModelDetachCredCards
import com.teamx.hatly.ui.fragments.payments.paymentmethod.modelGetCards.ModelCredCards
import com.teamx.hatly.ui.fragments.topUp.model.savedCard.ModelSavedCard
import com.teamx.hatlyUser.ui.fragments.auth.createpassword.model.ModelUpdatePass
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST(NetworkCallPoints.LOGIN_PHONE)
    suspend fun loginPhone(@Body params: JsonObject?): Response<LoginData>

    @POST(NetworkCallPoints.SIGN_UP)
    suspend fun signup(@Body params: JsonObject?): Response<RegisterData>

    @POST(NetworkCallPoints.OTP_VERIFY)
    suspend fun otpVerify(@Body params: JsonObject?): Response<LoginData>

    @POST(NetworkCallPoints.UPDATE_PASS)
    suspend fun updatePass(@Body params: JsonObject?): Response<ModelUpdatePass>

    @POST(NetworkCallPoints.VERIFY_FORGOT_PASS)
    suspend fun otpVerifyForgot(@Body params: JsonObject?): Response<ModelVerifyPassOtp>

    @PUT(NetworkCallPoints.CHANGE_PASSWORD)
    suspend fun changePassword(
        @Body params: JsonObject,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<SuccessData>


    @POST(NetworkCallPoints.RESEND_OTP_VERIFY)
    suspend fun resendOtp(@Body params: JsonObject?): Response<SuccessData>

    @POST(NetworkCallPoints.FORGOT_PASS_PHONE)
    suspend fun forgotPassPhone(@Body params: JsonObject?): Response<ForgotData>

    @POST(NetworkCallPoints.UPDATE_PROFILE)
    suspend fun updateProfile(
        @Body params: JsonObject,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<LoginData>


    @GET(NetworkCallPoints.NOTIFICATION_LIST)
    suspend fun notification(
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<ModelNotification>

    @POST(NetworkCallPoints.DEFAULT_CREDS_CARDS)
    suspend fun setDefaultCredCards(
        @Body params: JsonObject,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<ModelDefaultCredCards>

    @POST(NetworkCallPoints.DETACH_CREDS_CARDS)
    suspend fun setDetachCredCards(
        @Body params: JsonObject,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<ModelDetachCredCards>

    @GET(NetworkCallPoints.ME)
    suspend fun me(
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<MeModel>
 @GET(NetworkCallPoints.TRANSACTION_HISTORY)
    suspend fun getTransactionHistory(
     @Query("limit") limit: Int,
     @Query("page") page: Int,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<TransactionHistoryData>

    @GET(NetworkCallPoints.CREDS_CARDS)
    suspend fun getCredCards(
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<ModelCredCards>

    @POST(NetworkCallPoints.WALLET_TOPUP)
    suspend fun topUpSaved(
        @Body params: JsonObject,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<ModelSavedCard>


    @Multipart
    @POST(NetworkCallPoints.UPLOAD_REVIEW_IMGS)
    suspend fun uploadReviewImg(
        @Part images: List<MultipartBody.Part>,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<ModelUploadImages>

    @POST(NetworkCallPoints.FCM_TOKEN)
    suspend fun fcm(
        @Body params: JsonObject?,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<FcmModel>


    @POST(NetworkCallPoints.RESET_PASS_PHONE)
    suspend fun resetPassPhone(@Body params: JsonObject?): Response<ResetPassPhoneData>

    @GET(NetworkCallPoints.GET_PAST_ORDERS)
    suspend fun getPastOrders(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("status") status: String,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<PastOrdersData>
 @GET(NetworkCallPoints.GET_PAST_PARCELS)
    suspend fun getPastParcels(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("status") status: String,
     @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<GetPastParcelsData>

    @GET(NetworkCallPoints.GET_ORDERS_BYSTATUS)
    suspend fun getOrdersByStatus(
        @Query("status") status: String,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<PastOrdersData>

    @GET(NetworkCallPoints.GET_ACTIVE_ORDER)
    suspend fun getActiveOrders(
        @Query("status") status: String,
        @Query("requestFor") requestFor: String,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<GetActiveOrderData>


    @POST(NetworkCallPoints.ACCEPT_REJECT_ORDER)
    suspend fun acceptRejectOrder(
        @Path("id") id: String,
        @Body params: JsonObject?,
        @Header("Authorization") basicCredentials: String = "Bearer $TOKENER"
    ): Response<SuccessData>

}