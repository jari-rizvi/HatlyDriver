package com.teamx.hatly.ui.fragments.Auth.newPassword


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.teamx.hatly.baseclasses.BaseViewModel
import com.teamx.hatly.data.dataclasses.ResetPass.ResetPassPhoneData
import com.teamx.hatly.data.remote.Resource
import com.teamx.hatly.data.remote.reporitory.MainRepository
import com.teamx.hatly.utils.NetworkHelper
import com.teamx.hatly.utils.UnAuthorizedCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNewPassViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    private val _resetPassPhoneResponse = MutableLiveData<Resource<ResetPassPhoneData>>()
    val resetPassPhoneResponse: LiveData<Resource<ResetPassPhoneData>>
        get() = _resetPassPhoneResponse



    fun resetPassPhone(param: JsonObject, unAuthorizedCallback: UnAuthorizedCallback) {
        viewModelScope.launch {
            _resetPassPhoneResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.resetPassPhone(param).let {
                        if (it.isSuccessful) {
                            _resetPassPhoneResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 401) {
                            unAuthorizedCallback.onToSignUpPage()
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _resetPassPhoneResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            _resetPassPhoneResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _resetPassPhoneResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _resetPassPhoneResponse.postValue(Resource.error("No internet connection", null))
        }
    }


}