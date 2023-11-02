package com.teamx.hatly.ui.fragments.topUp


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.teamx.hatly.baseclasses.BaseViewModel
import com.teamx.hatly.data.remote.reporitory.MainRepository
import com.teamx.hatly.utils.NetworkHelper
import com.teamx.hatly.data.remote.Resource
import com.teamx.hatly.ui.fragments.payments.paymentmethod.modelGetCards.ModelCredCards
import com.teamx.hatly.ui.fragments.topUp.model.savedCard.ModelSavedCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject


@HiltViewModel
class TopUpModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    private val _credCardsGoogle = MutableLiveData<Resource<ModelCredCards>>()
    val credCardsResponse: LiveData<Resource<ModelCredCards>>
        get() = _credCardsGoogle
    fun credCards() {
        viewModelScope.launch {
            _credCardsGoogle.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getCredCards().let {
                        if (it.isSuccessful) {
                            _credCardsGoogle.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 422) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _credCardsGoogle.postValue(Resource.error(jsonObj.getString("message")))
                        } else {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _credCardsGoogle.postValue(Resource.error(jsonObj.getString("message")))
//                            _credCardsGoogle.postValue(Resource.error(it.message(), null))
                        }
                    }
                } catch (e: Exception) {
                    _credCardsGoogle.postValue(Resource.error("${e.message}", null))
                }
            } else _credCardsGoogle.postValue(Resource.error("No internet connection", null))
        }
    }


    private val _topUpSavedResponse = MutableLiveData<Resource<ModelSavedCard>>()
    val topUpSavedResponse: LiveData<Resource<ModelSavedCard>>
        get() = _topUpSavedResponse
    fun topUpSaved(params: JsonObject) {
        viewModelScope.launch {
            _topUpSavedResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.topUpSaved(params).let {
                        if (it.isSuccessful) {
                            _topUpSavedResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 422) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _topUpSavedResponse.postValue(Resource.error(jsonObj.getString("message")))
                        } else {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _topUpSavedResponse.postValue(Resource.error(jsonObj.getString("message")))
//                            _topUpSavedResponse.postValue(Resource.error(it.message(), null))
                        }
                    }
                } catch (e: Exception) {
                    _topUpSavedResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _topUpSavedResponse.postValue(Resource.error("No internet connection", null))
        }
    }
}