package com.teamx.hatly.ui.fragments.orders.Completed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.teamx.hatly.baseclasses.BaseViewModel
import com.teamx.hatly.data.dataclasses.getOrderStatus.GetOrderStatus
import com.teamx.hatly.data.remote.Resource
import com.teamx.hatly.data.remote.reporitory.MainRepository
import com.teamx.hatly.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CompletedViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {


    private val _getPastOrdersResponse = MutableLiveData<Resource<GetOrderStatus>>()
    val getOPastrdersResponse: LiveData<Resource<GetOrderStatus>>
        get() = _getPastOrdersResponse


    fun getOPastrders(status: String) {
        viewModelScope.launch {
            _getPastOrdersResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    Timber.tag("87878787887").d("starta")

                    mainRepository.getOrdersByStatus(status).let {
                        if (it.isSuccessful) {
                            _getPastOrdersResponse.postValue(Resource.success(it.body()!!))
                            Timber.tag("87878787887").d(it.body()!!.toString())
                        } else if (it.code() == 500 || it.code() == 409 || it.code() == 502 || it.code() == 404 || it.code() == 400) {
                            Timber.tag("87878787887").d("secoonnddd")

//                            _getPastOrdersResponse.postValue(Resource.error(it.message(), null))
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _getPastOrdersResponse.postValue(Resource.error(jsonObj.getString("message")))
                        } else {
                            _getPastOrdersResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                            Timber.tag("87878787887").d("third")

                        }
                    }
                } catch (e: Exception) {
                    _getPastOrdersResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _getPastOrdersResponse.postValue(Resource.error("No internet connection", null))
        }
    }
}