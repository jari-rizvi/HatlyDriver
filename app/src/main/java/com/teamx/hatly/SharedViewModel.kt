package com.teamx.hatly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.teamx.hatly.baseclasses.BaseViewModel
import com.teamx.hatly.data.models.ProductModel


/**
 * Shared View Model class for sharing data between fragments
 */
class SharedViewModel : BaseViewModel() {

    val clickOnContinueBtn: MutableLiveData<Boolean>? = null


}