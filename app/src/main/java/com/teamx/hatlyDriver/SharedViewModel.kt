package com.teamx.hatlyDriver

import androidx.lifecycle.MutableLiveData
import com.teamx.hatlyDriver.baseclasses.BaseViewModel
import com.teamx.hatlyDriver.ui.fragments.Dashboard.home.IncomingOrderSocketAdapter
import com.teamx.hatlyDriver.ui.fragments.Dashboard.home.IncomingParcelSocketAdapter
import com.teamx.hatlyDriver.ui.fragments.chat.socket.model.incomingOrderSocketData.Doc


/**
 * Shared View Model class for sharing data between fragments
 */
class SharedViewModel : BaseViewModel() {

    val clickOnContinueBtn: MutableLiveData<Boolean>? = null
    private val iAm = "working"

    var incomingOrderAdapter: IncomingOrderSocketAdapter? = null
     val incomingOrderSocketArrayList: ArrayList<Doc> = ArrayList<Doc>()
    var incomingParcelAdapter: IncomingParcelSocketAdapter? = null
     val incomingParcelSocketArrayList :ArrayList<com.teamx.hatlyDriver.ui.fragments.chat.socket.model.incomingParcelSoocketData.Doc> = ArrayList<com.teamx.hatlyDriver.ui.fragments.chat.socket.model.incomingParcelSoocketData.Doc>()


}