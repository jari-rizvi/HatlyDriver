package com.teamx.hatly.ui.fragments.orders.Incoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.teamx.hatly.baseclasses.BaseViewModel
import com.teamx.hatly.data.dataclasses.getOrderStatus.GetOrderStatus
import com.teamx.hatly.data.dataclasses.sucess.SuccessData
import com.teamx.hatly.data.remote.Resource
import com.teamx.hatly.data.remote.reporitory.MainRepository
import com.teamx.hatly.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class IncomingViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {


    private val _getIncomingOrdersResponse = MutableLiveData<Resource<GetOrderStatus>>()
    val getOPastrdersResponse: LiveData<Resource<GetOrderStatus>>
        get() = _getIncomingOrdersResponse


    fun getIncomingOrders(status: String) {
        viewModelScope.launch {
            _getIncomingOrdersResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    Timber.tag("87878787887").d("starta")

                    mainRepository.getOrdersByStatus(status).let {
                        if (it.isSuccessful) {
                            _getIncomingOrdersResponse.postValue(Resource.success(it.body()!!))
                            Timber.tag("87878787887").d(it.body()!!.toString())
                        } else if (it.code() == 500 || it.code() == 409 || it.code() == 502 || it.code() == 404 || it.code() == 400) {
                            Timber.tag("87878787887").d("secoonnddd")

//                            _getIncomingOrdersResponse.postValue(Resource.error(it.message(), null))
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _getIncomingOrdersResponse.postValue(Resource.error(jsonObj.getString("message")))
                        } else {
                            _getIncomingOrdersResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                            Timber.tag("87878787887").d("third")

                        }
                    }
                } catch (e: Exception) {
                    _getIncomingOrdersResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _getIncomingOrdersResponse.postValue(
                Resource.error(
                    "No internet connection",
                    null
                )
            )
        }
    }


    private val _acceptRejectResponse = MutableLiveData<Resource<SuccessData>>()
    val acceptRejectResponse: LiveData<Resource<SuccessData>>
        get() = _acceptRejectResponse

    fun acceptReject(id: String, param: JsonObject) {
        viewModelScope.launch {
            _acceptRejectResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.acceptRejectOrder(id, param).let {
                        if (it.isSuccessful) {
                            _acceptRejectResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 409 || it.code() == 502 || it.code() == 404 || it.code() == 400 || it.code() == 422) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _acceptRejectResponse.postValue(Resource.error(jsonObj.getString("message")))
                        } else {
                            _acceptRejectResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _acceptRejectResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _acceptRejectResponse.postValue(Resource.error("No internet connection", null))
        }
    }


}