package com.teamx.hatlyDriver.ui.fragments.wallet.withdraw


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.teamx.hatlyDriver.baseclasses.BaseViewModel
import com.teamx.hatlyDriver.data.dataclasses.withdrawalData.WithDrawalData
import com.teamx.hatlyDriver.data.dataclasses.withdrawalDetails.WithDrawalDetailsData
import com.teamx.hatlyDriver.data.remote.Resource
import com.teamx.hatlyDriver.data.remote.reporitory.MainRepository
import com.teamx.hatlyDriver.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject


@HiltViewModel
class WithdrawalViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {


    private val _withdrawalDetailsResponse = MutableLiveData<Resource<WithDrawalDetailsData>>()
    val withdrawalDetailsResponse: LiveData<Resource<WithDrawalDetailsData>>
        get() = _withdrawalDetailsResponse

    fun withdrawalDetails(id: String) {
        viewModelScope.launch {
            _withdrawalDetailsResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.withdrawalDetails(id).let {
                        if (it.isSuccessful) {
                            _withdrawalDetailsResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 401) {
                            _withdrawalDetailsResponse.postValue(Resource.unAuth("", null))
                        } else if (it.code() == 500 || it.code() == 409 || it.code() == 502 || it.code() == 404 || it.code() == 400 || it.code() == 422) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _withdrawalDetailsResponse.postValue(
                                Resource.error(
                                    jsonObj.getString(
                                        "message"
                                    )
                                )
                            )
                        } else {
                            _withdrawalDetailsResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _withdrawalDetailsResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _withdrawalDetailsResponse.postValue(
                Resource.error(
                    "No internet connection",
                    null
                )
            )
        }
    }






    private val _withdrawalResponse = MutableLiveData<Resource<WithDrawalData>>()
    val withdrawalResponse: LiveData<Resource<WithDrawalData>>
        get() = _withdrawalResponse

    fun withdrawal(params: JsonObject) {
        viewModelScope.launch {
            _withdrawalResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.createWithdrawal(params).let {
                        if (it.isSuccessful) {
                            _withdrawalResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 422) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _withdrawalResponse.postValue(Resource.error(jsonObj.getString("message")))
                        } else {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _withdrawalResponse.postValue(Resource.error(jsonObj.getString("message")))
//                            _withdrawalResponse.postValue(Resource.error(it.message(), null))
                        }
                    }
                } catch (e: Exception) {
                    _withdrawalResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _withdrawalResponse.postValue(Resource.error("No internet connection", null))
        }
    }
}