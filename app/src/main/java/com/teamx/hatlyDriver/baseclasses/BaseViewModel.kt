package com.teamx.hatlyDriver.baseclasses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamx.hatlyDriver.data.dataclasses.login.LoginData


open class BaseViewModel : ViewModel() {

    private val _userShared = MutableLiveData<LoginData>()

    val userData: LiveData<LoginData>
        get() = _userShared

    fun setUserData(_userId: LoginData) {
        this._userShared.value = _userId
    }

}