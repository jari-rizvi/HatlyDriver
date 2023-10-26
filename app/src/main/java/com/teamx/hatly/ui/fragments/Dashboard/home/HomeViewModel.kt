package com.teamx.hatly.ui.fragments.Dashboard.home


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.teamx.hatly.baseclasses.BaseViewModel
import com.teamx.hatly.data.dataclasses.getorders.GetAllOrdersData
import com.teamx.hatly.data.remote.Resource
import com.teamx.hatly.data.remote.reporitory.MainRepository
import com.teamx.hatly.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {


    private val _getOrdersResponse = MutableLiveData<Resource<GetAllOrdersData>>()
    val getOrdersResponse: LiveData<Resource<GetAllOrdersData>>
        get() = _getOrdersResponse


    fun getOrders(/*limit: String, page: String, orderType: String*/) {
        viewModelScope.launch {
            _getOrdersResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    Timber.tag("87878787887").d("starta")

                    mainRepository.getOrders(/*limit, page, orderType*/).let {
                        if (it.isSuccessful) {
                            _getOrdersResponse.postValue(Resource.success(it.body()!!))
                            Timber.tag("87878787887").d(it.body()!!.toString())
                        } else if (it.code() == 500 || it.code() == 409 || it.code() == 502 || it.code() == 404 || it.code() == 400) {
                            Timber.tag("87878787887").d("secoonnddd")

//                            _getOrdersResponse.postValue(Resource.error(it.message(), null))
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _getOrdersResponse.postValue(Resource.error(jsonObj.getString("message")))
                        } else {
                            _getOrdersResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                            Timber.tag("87878787887").d("third")

                        }
                    }
                } catch (e: Exception) {
                    _getOrdersResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _getOrdersResponse.postValue(Resource.error("No internet connection", null))
        }
    }
}