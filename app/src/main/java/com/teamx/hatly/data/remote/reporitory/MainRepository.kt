package com.teamx.hatly.data.remote.reporitory

import com.teamx.hatly.data.local.db.AppDao
import com.teamx.hatly.data.local.dbModel.CartDao
import com.teamx.hatly.data.remote.ApiService
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService, var localDataSource: AppDao, var localDataSource2: CartDao
) {


}