package com.teamx.hatly.ui.fragments.orders.Completed

import com.teamx.hatly.baseclasses.BaseViewModel
import com.teamx.hatly.data.remote.reporitory.MainRepository
import com.teamx.hatly.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompletedViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {



}