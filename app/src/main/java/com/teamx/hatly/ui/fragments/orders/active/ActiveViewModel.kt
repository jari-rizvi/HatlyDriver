package com.teamx.hatly.ui.fragments.orders.active

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.teamx.hatly.baseclasses.BaseViewModel
import com.teamx.hatly.data.dataclasses.getActiveorder.GetActiveOrderData
import com.teamx.hatly.data.remote.Resource
import com.teamx.hatly.data.remote.reporitory.MainRepository
import com.teamx.hatly.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ActiveViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {
    private val _getActiveOrderResponse = MutableLiveData<Resource<GetActiveOrderData>>()
    val getActiveOrderResponse: LiveData<Resource<GetActiveOrderData>>
        get() = _getActiveOrderResponse


    fun getActiveOrder(status: String,requestFor : String) {
        viewModelScope.launch {
            _getActiveOrderResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    Timber.tag("87878787887").d("starta")

                    mainRepository.getActiveOrders(status,requestFor).let {
                        if (it.isSuccessful) {
                            _getActiveOrderResponse.postValue(Resource.success(it.body()!!))
                            Timber.tag("87878787887").d(it.body()!!.toString())
                        } else if (it.code() == 500 || it.code() == 409 || it.code() == 502 || it.code() == 404 || it.code() == 400) {
                            Timber.tag("87878787887").d("secoonnddd")

//                            _getActiveOrderResponse.postValue(Resource.error(it.message(), null))
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _getActiveOrderResponse.postValue(Resource.error(jsonObj.getString("message")))
                        } else {
                            _getActiveOrderResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                            Timber.tag("87878787887").d("third")

                        }
                    }
                } catch (e: Exception) {
                    _getActiveOrderResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _getActiveOrderResponse.postValue(Resource.error("No internet connection", null))
        }
    }


}