package com.teamx.hatlyDriver.ui.fragments.wallet


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.teamx.hatlyDriver.baseclasses.BaseViewModel
import com.teamx.hatlyDriver.data.dataclasses.meModel.me.MeModel
import com.teamx.hatlyDriver.data.dataclasses.sucess.SuccessData
import com.teamx.hatlyDriver.data.dataclasses.transactionHistory.TransactionHistoryData
import com.teamx.hatlyDriver.data.dataclasses.withdrawalHistory.WithDrawalHistory
import com.teamx.hatlyDriver.data.remote.Resource
import com.teamx.hatlyDriver.data.remote.reporitory.MainRepository
import com.teamx.hatlyDriver.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject


@HiltViewModel
class WalletViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {



    private val _bankDetailsResponse = MutableLiveData<Resource<SuccessData>>()
    val bankDetailsResponse: LiveData<Resource<SuccessData>>
        get() = _bankDetailsResponse

    fun addBankDetails(param: JsonObject/*, unAuthorizedCallback: UnAuthorizedCallback*/) {
        viewModelScope.launch {
            _bankDetailsResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.addBankDetails(param).let {
                        if (it.isSuccessful) {
                            _bankDetailsResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 401) {
                            _bankDetailsResponse.postValue(Resource.error(it.message(), null))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _bankDetailsResponse.postValue(Resource.error(jsonObj.getString("message")))
//                            _bankDetailsResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            _bankDetailsResponse.postValue(Resource.error("Some thing went wrong", null))
                        }
                    }
                } catch (e: Exception) {
                    _bankDetailsResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _bankDetailsResponse.postValue(Resource.error("No internet connection", null))
        }
    }



    private val _meResponse = MutableLiveData<Resource<MeModel>>()
    val meResponse: LiveData<Resource<MeModel>>
        get() = _meResponse

    fun me() {
        viewModelScope.launch {
            _meResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.me().let {
                        if (it.isSuccessful) {
                            _meResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 422) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _meResponse.postValue(Resource.error(jsonObj.getString("message")))
                        } else {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _meResponse.postValue(Resource.error(jsonObj.getString("message")))
//                            _meResponse.postValue(Resource.error(it.message(), null))
                        }
                    }
                } catch (e: Exception) {
                    _meResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _meResponse.postValue(Resource.error("No internet connection", null))
        }
    }


    private val transactionHistoryeResponse = MutableLiveData<Resource<TransactionHistoryData>>()
    val transactionHistoryResponse: LiveData<Resource<TransactionHistoryData>>
        get() = transactionHistoryeResponse

    fun trancationHisotory(limit: Int, page: Int) {
        viewModelScope.launch {
            transactionHistoryeResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getTransactionHistory(limit, page).let {
                        if (it.isSuccessful) {
                            transactionHistoryeResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 401) {
                            transactionHistoryeResponse.postValue(Resource.unAuth("", null))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 422) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            transactionHistoryeResponse.postValue(Resource.error(jsonObj.getString("message")))
                        } else {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            transactionHistoryeResponse.postValue(Resource.error(jsonObj.getString("message")))
//                            transactionHistoryeResponse.postValue(Resource.error(it.message(), null))
                        }
                    }
                } catch (e: Exception) {
                    transactionHistoryeResponse.postValue(Resource.error("${e.message}", null))
                }
            } else transactionHistoryeResponse.postValue(
                Resource.error(
                    "No internet connection",
                    null
                )
            )
        }
    }

    private val withdrawalResponse = MutableLiveData<Resource<WithDrawalHistory>>()
    val withdrawalHistoryResponse: LiveData<Resource<WithDrawalHistory>>
        get() = withdrawalResponse

    fun withdrawalHisotory(limit: Int, page: Int) {
        viewModelScope.launch {
            withdrawalResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getWithdrawalHistory(limit, page).let {
                        if (it.isSuccessful) {
                            withdrawalResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 401) {
                            withdrawalResponse.postValue(Resource.unAuth("", null))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 422) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            withdrawalResponse.postValue(Resource.error(jsonObj.getString("message")))
                        } else {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            withdrawalResponse.postValue(Resource.error(jsonObj.getString("message")))
//                            withdrawalResponse.postValue(Resource.error(it.message(), null))
                        }
                    }
                } catch (e: Exception) {
                    withdrawalResponse.postValue(Resource.error("${e.message}", null))
                }
            } else withdrawalResponse.postValue(
                Resource.error(
                    "No internet connection",
                    null
                )
            )
        }
    }




}