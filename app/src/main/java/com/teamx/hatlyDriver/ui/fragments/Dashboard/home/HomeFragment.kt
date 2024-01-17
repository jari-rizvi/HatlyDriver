package com.teamx.hatlyDriver.ui.fragments.Dashboard.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.teamx.hatlyDriver.BR
import com.teamx.hatlyDriver.R
import com.teamx.hatlyDriver.baseclasses.BaseFragment
import com.teamx.hatlyDriver.constants.NetworkCallPoints
import com.teamx.hatlyDriver.data.dataclasses.getOrderStatus.Doc
import com.teamx.hatlyDriver.data.remote.Resource
import com.teamx.hatlyDriver.databinding.FragmentHomeBinding
import com.teamx.hatlyDriver.ui.fragments.chat.socket.IncomingOrderCallBack
import com.teamx.hatlyDriver.ui.fragments.chat.socket.RiderSocketClass
import com.teamx.hatlyDriver.ui.fragments.chat.socket.model.alreadyAccept.AlreadyAcceptedData
import com.teamx.hatlyDriver.ui.fragments.chat.socket.model.incomingOrderSocketData.IncomingOrdersSocketData
import com.teamx.hatlyDriver.ui.fragments.chat.socket.model.incomingParcelSoocketData.IncomingParcelSocketData
import com.teamx.hatlyDriver.ui.fragments.orders.Incoming.onAcceptReject
import com.teamx.hatlyDriver.utils.DialogHelperClass
import com.teamx.hatlyDriver.utils.PrefHelper
import com.teamx.hatlyDriver.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(),
    DialogHelperClass.Companion.ReasonDialog,
    DialogHelperClass.Companion.OfflineReasonDialog,
    onAcceptReject, DialogHelperClass.Companion.ConfirmLocationDialog, IncomingOrderCallBack,
    onAcceptRejectSocket, onAcceptRejectParcel,
    DialogHelperClass.Companion.DialogProminentInterface {

    override val layoutId: Int
        get() = R.layout.fragment_home
    override val viewModel: Class<HomeViewModel>
        get() = HomeViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var id: String

    lateinit var handler: Handler
    lateinit var runnable: Runnable
    var earning: String = "earning"

    private lateinit var seekBar1: SeekBar
    private lateinit var statusText: TextView

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var locationRequest: LocationRequest? = null


    private var originLatitude: String = "0.0"
    private var originLongitude: String = "0.0"


    lateinit var pastparcelArrayList: ArrayList<com.teamx.hatlyDriver.data.dataclasses.pastParcels.Doc>
    lateinit var pastOrderArrayList: ArrayList<com.teamx.hatlyDriver.data.dataclasses.pastorder.Doc>

    lateinit var pastOrderAdapter: PastOrderAdapter
    lateinit var pastParcelAdapter: PastParcelAdapter


    var isTrueOrder: Boolean = false
    var isTrueParcel: Boolean = false


    var type: String = ""
    var userimg: String = ""
    var username: String = ""

//    lateinit var sharedViewModel.incomingOrderAdapter: IncomingOrderSocketAdapter
//    lateinit var sharedViewModel.incomingParcelAdapter: IncomingParcelSocketAdapter
//    lateinit var sharedViewModel.incomingOrderSocketArrayList: ArrayList<com.teamx.hatlyDriver.ui.fragments.chat.socket.model.incomingOrderSocketData.Doc>
//    lateinit var sharedViewModel.incomingParcelSocketArrayList: ArrayList<com.teamx.hatlyDriver.ui.fragments.chat.socket.model.incomingParcelSoocketData.Doc>

    lateinit var incomingOrderArrayList: ArrayList<Doc>

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.lifecycleOwner = viewLifecycleOwner
        incomingOrderArrayList = ArrayList()
        options = navOptions {
            anim {
                enter = R.anim.enter_from_left
                exit = R.anim.exit_to_left
                popEnter = R.anim.nav_default_pop_enter_anim
                popExit = R.anim.nav_default_pop_exit_anim
            }
        }

        handler = Handler()

        val userData = PrefHelper.getInstance(requireActivity()).getUserData()

        userData?.let {
            Log.d("setUserData", "onViewCreated: ${it}")
            sharedViewModel.setUserData(it)

        }


        /*   try {

               var bundle = arguments
               if (bundle == null) {
                   bundle = Bundle()
               }
               userimg = bundle?.getString("userimg").toString()
               username = bundle?.getString("username").toString()

               mViewDataBinding.name.text = "Hello " + username
               mViewDataBinding.userProfile
               Picasso.get().load(userimg).resize(500, 500)
                   .into(mViewDataBinding.profilePicture)
           } catch (e: Exception) {
           }*/

        apiCalls()


        mViewDataBinding.profilePicture.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.editProfileFragment, arguments, options)
//            RiderSocketClass.disconnect()
        }

        mViewDataBinding.constraintLayout1.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.notificaitonFragment, arguments, options)
//            RiderSocketClass.disconnect()
        }
        mViewDataBinding.constraintLayout.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.profileFragment, arguments, options)
//            RiderSocketClass.disconnect()
        }

        mViewDataBinding.btnPastParcelAll.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.parcelFragment, arguments, options)
//            RiderSocketClass.disconnect()
        }

        mViewDataBinding.textView18.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.parcelFragment, arguments, options)
//            RiderSocketClass.disconnect()
        }

        mViewDataBinding.btnPastOrderAll.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.orderFragment, arguments, options)
//            RiderSocketClass.disconnect()
        }

        mViewDataBinding.textView20.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.orderFragment, arguments, options)
//            RiderSocketClass.disconnect()
        }


        getDeviceLocation()

//        // Runnable ko postDelayed ke zariye schedule karen
//        handler.postDelayed(runnable, 2000)

        Firebase.initialize(requireContext())
        FirebaseApp.initializeApp(requireContext())
        if (!mViewModel.fcmResponse.hasActiveObservers()) {
            getFcmToken()
        }

        pushNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)

        if (!mViewModel.fcmResponse.hasActiveObservers()) {
            mViewModel.fcmResponse.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.AUTH -> {
                        loadingDialog.dismiss()
                        onToSignUpPage()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
//                            mViewDataBinding.root.snackbar(data.message)
                        }
                    }

                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        mViewDataBinding.root.snackbar(it.message!!)
                    }
                }
            }
        }


        OrderRecyclerview()
        ParcelRecyclerview()
        PastOrderRecyclerview()
        PastParcelRecyclerview()

        seekBar1 = mViewDataBinding.slider
        statusText = mViewDataBinding.statusText



        seekBar1.isClickable = false

        val seekbarValue = PrefHelper.getInstance(requireContext()).getMax

        if (seekbarValue == 100) {
            mViewDataBinding.connnnn.visibility = View.VISIBLE

            RiderSocketClass.connectRider(
                NetworkCallPoints.TOKENER,
                originLatitude,
                originLongitude,
                this@HomeFragment
            )

            mViewDataBinding.btnPastOrderAll.visibility = View.VISIBLE
            mViewDataBinding.btnPastParcelAll.visibility = View.VISIBLE
        }


        val seekbarText = PrefHelper.getInstance(requireContext()).getSeekbarText

        Log.d("TAG", "seekbarValue: $seekbarValue")

        if (seekbarValue != null) {
            seekBar1.progress = seekbarValue
            mViewDataBinding.slider2.progress = seekbarValue
            statusText.text = seekbarText

        }





        seekBar1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                seekBar.isEnabled = true

                if (progress == seekBar.max) {

                    DialogHelperClass.confirmLocation(
                        requireContext(), this@HomeFragment, true
                    )
                    mViewDataBinding.btnPastOrderAll.visibility = View.VISIBLE
                    mViewDataBinding.btnPastParcelAll.visibility = View.VISIBLE

                    seekBar.thumb = resources.getDrawable(R.drawable.custom_thumb, null)
                    statusText.text = getString(R.string.go_offline)
                    PrefHelper.getInstance(requireContext())
                        .saveSeekbarText(statusText.text.toString())


                } else {
                    // Reset thumb color to the default
                    seekBar.thumb = resources.getDrawable(R.drawable.custom_thumb, null)

//                    RiderSocketClass.disconnect()
                    // Hide "Go Online" text
                    mViewDataBinding.btnPastOrderAll.visibility = View.GONE
                    mViewDataBinding.btnPastParcelAll.visibility = View.GONE
                    statusText.text = getString(R.string.go_online)
                    PrefHelper.getInstance(requireContext())
                        .saveSeekbarText(statusText.text.toString())

                }
                mViewDataBinding.slider2.progress = seekBar.progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                mViewDataBinding.slider2.progress = seekBar.progress
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

                if (seekBar1.progress > 50) {
                    seekBar1.progress = seekBar.max
                    PrefHelper.getInstance(requireContext())
                        .isMax(seekBar.max)
                    PrefHelper.getInstance(requireContext())
                        .saveSeekbarText(statusText.text.toString())

                    RiderSocketClass.connectRider(
                        NetworkCallPoints.TOKENER,
                        originLatitude,
                        originLongitude,
                        this@HomeFragment
                    )
                    mViewDataBinding.connnnn.visibility = View.VISIBLE


                } else {
                    seekBar1.progress = seekBar.min
                    PrefHelper.getInstance(requireContext())
                        .isMax(seekBar.min)
                    PrefHelper.getInstance(requireContext())
                        .saveSeekbarText(statusText.text.toString())

                    DialogHelperClass.submitOfflineReason(
                        requireContext(), this@HomeFragment, true, "", ""
                    )
                }
                mViewDataBinding.slider2.progress = seekBar1.progress
            }

        })


        val spinner = mViewDataBinding.spinner1
//        val spinner1 = mViewDataBinding.spinner1

        try {


            val items = arrayOf("weekly", "monthly", "yearly")

            val adapter = ArrayAdapter(requireContext(), R.layout.custom_spinner_item, items)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        } catch (e: Exception) {

        }


//        val adapter = ArrayAdapter.createFromResource(
//            requireContext(),
//            R.array.spinner_items,
//            R.layout.custom_spinner_item
//        )

//        adapter.setDropDownViewResource(R.layout.custom_spinner_item)


//        spinner1.adapter = adapter
        try {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = parent.getItemAtPosition(position) as String
//                earning = "earning"

                    val params = JsonObject()
                    try {
                        params.addProperty("filterBy", selectedItem)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    mViewModel.getTotalEarnings(params)

//                mViewModel.getTotalEarnings(selectedItem, earning)


                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }



        Log.d("runnableIs", "Runnable1st: ")

        runnable = Runnable {
            getDeviceLocation()
            Log.d("runnableIs", "Runnable2nd: ")
            RiderSocketClass.updateLocation(originLatitude, originLongitude)

            handler.postDelayed(runnable, 3000)
        }


    }

    fun apiCalls() {
        mViewModel.getPastParcels(1, 5, "delivered")

        if (!mViewModel.getPastParcelsResponse.hasActiveObservers()) {
            mViewModel.getPastParcelsResponse.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.AUTH -> {
                        loadingDialog.dismiss()
                        onToSignUpPage()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            data.docs.forEach {
                                pastparcelArrayList.add(it)
                            }

                            pastParcelAdapter.notifyDataSetChanged()


                        }
                    }

                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(
                            requireContext(),
                            it.message!!
                        )
                    }
                }
            }
        }


        if (!mViewModel.totalEarningsResponse.hasActiveObservers()) {
            mViewModel.totalEarningsResponse.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.AUTH -> {
                        loadingDialog.dismiss()
                        onToSignUpPage()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->

                            try {
                                mViewDataBinding.hours.text =
                                    data.totalSpendTime.hours.toString() + "h " + data.totalSpendTime.minuts + "m"
                                mViewDataBinding.name.text =
                                    "Hello " + data.userId.name as String
                                Picasso.get().load(data.userId.profileImage).resize(500, 500)
                                    .into(mViewDataBinding.profilePicture)

                                mViewDataBinding.totalEarning.text = data.totalEarning.toString()
                                mViewDataBinding.totalorders.text = data.totalOrders.toString()
                                mViewDataBinding.totalParcels.text = data.totalParcels.toString()
                                mViewDataBinding.totalOrderEarning.text =
                                    data.totalOrdersEarning.toString()
                                mViewDataBinding.totalParcelsEarning.text =
                                    data.totalParcelsEarning.toString()


                            } catch (e: Exception) {
                            }


                        }
                    }

                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(
                            requireContext(),
                            it.message!!
                        )
                    }
                }
            }
        }

        mViewModel.getPastOrders(1, 5, "delivered")

        if (!mViewModel.getPastOrdersResponse.hasActiveObservers()) {
            mViewModel.getPastOrdersResponse.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.AUTH -> {
                        loadingDialog.dismiss()
                        onToSignUpPage()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            data.docs.forEach {
                                pastOrderArrayList.add(it)
                            }

                            pastOrderAdapter.notifyDataSetChanged()


                        }
                    }

                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(
                            requireContext(),
                            it.message!!
                        )
                    }
                }
            }
        }


    }


    private fun OrderRecyclerview() {
//        sharedViewModel.incomingOrderSocketArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        mViewDataBinding.recyclerViewIncomingOrders.layoutManager = linearLayoutManager

        sharedViewModel.incomingOrderAdapter =
            IncomingOrderSocketAdapter(sharedViewModel.incomingOrderSocketArrayList, this)
        mViewDataBinding.recyclerViewIncomingOrders.adapter = sharedViewModel.incomingOrderAdapter

    }

    private fun ParcelRecyclerview() {
//        sharedViewModel.incomingParcelSocketArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        mViewDataBinding.recyclerViewSpecialOrders.layoutManager = linearLayoutManager

        sharedViewModel.incomingParcelAdapter =
            IncomingParcelSocketAdapter(sharedViewModel.incomingParcelSocketArrayList, this)
        mViewDataBinding.recyclerViewSpecialOrders.adapter = sharedViewModel.incomingParcelAdapter

    }

    private fun PastParcelRecyclerview() {
        pastparcelArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        mViewDataBinding.recyclerViewSpecialPastOrders.layoutManager = linearLayoutManager

        pastParcelAdapter = PastParcelAdapter(pastparcelArrayList)
        mViewDataBinding.recyclerViewSpecialPastOrders.adapter = pastParcelAdapter

    }

    private fun PastOrderRecyclerview() {
        pastOrderArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        mViewDataBinding.recyclerViewPastOrders.layoutManager = linearLayoutManager

        pastOrderAdapter = PastOrderAdapter(pastOrderArrayList)
        mViewDataBinding.recyclerViewPastOrders.adapter = pastOrderAdapter

    }


    var locationPermissionGranted = true

    companion object {
        var mMap: GoogleMap? = null
    }


    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {

                fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(requireActivity())
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    if (it != null) {
                        Timber.tag("TAG")
                            .d("onCreate:latitude${it.latitude}longitude${it.longitude} ")
                    }
                }
                locationRequest = LocationRequest()
                locationRequest?.interval = 10
                locationRequest?.fastestInterval = 10
                locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                fusedLocationClient.requestLocationUpdates(
                    locationRequest!!, object : LocationCallback() {

                    }, Looper.getMainLooper()
                )

                val locationResult = fusedLocationClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        val lastKnownLocation = task.result
                        if (lastKnownLocation != null) {

                            originLatitude = lastKnownLocation.latitude.toString()
                            originLongitude = lastKnownLocation.longitude.toString()

                            Timber.tag("lastKnownLocation").d(
                                "Current location is . Using defaults. ${lastKnownLocation.latitude}  ${lastKnownLocation.longitude}"
                            )


                        }
                    } else {
                        Timber.tag("TAG").d("Current location is null. Using defaults.")
                        Timber.tag("TAG").d("Exception:   ${task.exception}")
                        mMap?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }


        } catch (e: SecurityException) {
            Timber.tag("Exception: %s").e(e, e.message)
        }
    }


    override fun onConfirmLocation() {

        DialogHelperClass.prominentDialog(requireActivity(), this)


    }


    private val PERMISSION_REQUEST_CODE = 123

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission has already been granted
            RiderSocketClass.connectRider(
                NetworkCallPoints.TOKENER,
                originLatitude,
                originLongitude,
                this
            )
            handler.postDelayed(runnable, 3000)





            /* Firebase.initialize(requireContext())
             FirebaseApp.initializeApp(requireContext())

             if (!mViewModel.fcmResponse.hasActiveObservers()) {
                 askNotificationPermission()
             }*/

            /*DialogHelperClass.confirmLocation(
                requireContext(), this@HomeFragment, true*/


//            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
//            navController.navigate(R.id.dashboard, null, options)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            RiderSocketClass.connectRider(
                NetworkCallPoints.TOKENER,
                originLatitude,
                originLongitude, this
            )
            handler.postDelayed(runnable, 3000)


        } else {
            val snackbar = Snackbar.make(
                mViewDataBinding.root, "Permission required to proceed..", Snackbar.LENGTH_SHORT
            )
            snackbar.setAction("Settings") {
                //
                Timber.tag("TAG").d("ScankonRequestPermissionsResult: ")/*        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", requireActivity().packageName, null)
                        intent.data = uri
                        requireActivity().startActivity(intent)*/
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context?.packageName, null)
                intent.data = uri
                startActivity(intent)
                //
//                snackbar.dismiss()
            }
            snackbar.show()


        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onAlreadyAccepted(alreadyAcceptedData: AlreadyAcceptedData) {
        try {
            mViewModel.viewModelScope.launch(Dispatchers.Main) {
                Timber.tag("MessageSocketClass")
                    .d("REQUEST_ACCEPTED5: ${alreadyAcceptedData.requestedId}")
                val accepted_id = alreadyAcceptedData.requestedId

                sharedViewModel.incomingOrderSocketArrayList.removeIf {
                    it._id == accepted_id
                }
                sharedViewModel.incomingParcelSocketArrayList.removeIf {
                    it._id == accepted_id
                }


                sharedViewModel.incomingOrderAdapter?.notifyDataSetChanged()
                sharedViewModel.incomingParcelAdapter?.notifyDataSetChanged()
                mViewDataBinding.recyclerViewIncomingOrders.adapter?.notifyDataSetChanged()
                mViewDataBinding.recyclerViewSpecialOrders.adapter?.notifyDataSetChanged()


                if (isTrueOrder == true) {
                    navController =
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    navController.navigate(R.id.orderFragment, null, options)
                }

                if (isTrueParcel == true) {
                    navController =
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    navController.navigate(R.id.parcelFragment, null, options)
                }


            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun onIncomingOrderRecieve(incomingOrderSocketData: com.teamx.hatlyDriver.ui.fragments.chat.socket.model.incomingOrderSocketData.Doc) {
        /*    sharedViewModel.incomingOrderSocketArrayList.clear()*/

        Log.d("TAG", "onIncomingOrderRecieveSinglre:")

        GlobalScope.launch(Dispatchers.Main) {
            Log.d("TAG", "onIncomingOrderRecieveSinglre:")

            sharedViewModel.incomingOrderSocketArrayList.add(0, incomingOrderSocketData)


            mViewDataBinding.recyclerViewIncomingOrders.adapter?.notifyDataSetChanged()
        }
    }

    override fun onGetAllOrderRecieve(incomingOrderSocketData: IncomingOrdersSocketData) {
//        sharedViewModel.incomingOrderSocketArrayList.clear()
        Log.d("TAG", "onIncomingOrderRecieveAll:")

        GlobalScope.launch(Dispatchers.Main) {

            sharedViewModel.incomingOrderSocketArrayList.addAll(incomingOrderSocketData.docs)
            Log.d("TAG", "onIncomingOrderRecieveAll:")



            mViewDataBinding.recyclerViewIncomingOrders.adapter?.notifyDataSetChanged()
        }

    }

    override fun onGetAllParcelRecieve(incomingParcelSocketData: IncomingParcelSocketData) {
        sharedViewModel.incomingParcelSocketArrayList.clear()
        GlobalScope.launch(Dispatchers.Main) {

            sharedViewModel.incomingParcelSocketArrayList.addAll(incomingParcelSocketData.docs)


            mViewDataBinding.recyclerViewSpecialOrders.adapter?.notifyDataSetChanged()

        }
    }

    override fun onIncomingParcelRecieve(incomingParcelSocketData: com.teamx.hatlyDriver.ui.fragments.chat.socket.model.incomingParcelSoocketData.Doc) {
        sharedViewModel.incomingParcelSocketArrayList.clear()
        GlobalScope.launch(Dispatchers.Main) {

            sharedViewModel.incomingParcelSocketArrayList.add(0, incomingParcelSocketData)


            mViewDataBinding.recyclerViewSpecialOrders.adapter?.notifyDataSetChanged()

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onAcceptSokcetClick(position: Int) {
        id = sharedViewModel.incomingOrderSocketArrayList[position]._id

        mViewModel.acceptOrder(id)

        mViewModel.acceptResponse.observe(requireActivity(), Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }

                Resource.Status.AUTH -> {
                    loadingDialog.dismiss()
                    onToSignUpPage()
                }

                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    it.data?.let { data ->
                        val incomingOrderSocketArrayList1 =
                            sharedViewModel.incomingOrderSocketArrayList.filter {
                                it._id != id
                            }
                        sharedViewModel.incomingOrderSocketArrayList.clear()
                        sharedViewModel.incomingOrderSocketArrayList.addAll(
                            incomingOrderSocketArrayList1
                        )

                        isTrueOrder = true

//                        showToast(data.message)

                        sharedViewModel.incomingOrderAdapter?.notifyDataSetChanged()
                    }
                }

                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogHelperClass.errorDialog(requireContext(), it.message!!)
                }
            }
        })
    }

    override fun onRejectSocketClick(position: Int) {
        id = sharedViewModel.incomingOrderSocketArrayList[position]._id

        DialogHelperClass.submitReason(
            requireContext(), this, true, "", ""
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSubmitClick(rejectionReason: String) {

        val params = JsonObject()
        try {
            params.addProperty("reasion", rejectionReason)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        mViewModel.rejectOrder(id, params)

        mViewModel.rejectesponse.observe(requireActivity(), Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }

                Resource.Status.AUTH -> {
                    loadingDialog.dismiss()
                    onToSignUpPage()
                }

                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    it.data?.let { data ->
                        val incomingOrderSocketArrayList1 =
                            sharedViewModel.incomingOrderSocketArrayList.filter {
                                it._id != id
                            }


                        sharedViewModel.incomingOrderSocketArrayList.removeLast()

//                        sharedViewModel.incomingOrderSocketArrayList.clear()
//                        sharedViewModel.incomingOrderSocketArrayList.addAll(sharedViewModel.incomingOrderSocketArrayList1)
//                        showToast(data.message)
                        sharedViewModel.incomingOrderAdapter?.notifyDataSetChanged()

                        /*  navController =
                              Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                          navController.navigate(R.id.orderFragment, null, options)*/


                    }
                }

                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogHelperClass.errorDialog(requireContext(), it.message!!)
                }
            }
        })
    }

    override fun onCancelClick() {
    }


    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("123123", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val params = JsonObject()
            params.addProperty("fcmToken", task.result)


            mViewModel.fcm(params)
            Log.d("fcmToken", "gaya ${params}")


        })

    }


    override fun onAcceptClick(position: Int) {
    }

    override fun onRejectClick(position: Int) {
    }

    override fun onAcceptParcelClick(position: Int) {
        id = sharedViewModel.incomingParcelSocketArrayList[position]._id

        mViewModel.acceptOrder(id)

        mViewModel.acceptResponse.observe(requireActivity(), Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }

                Resource.Status.AUTH -> {
                    loadingDialog.dismiss()
                    onToSignUpPage()
                }

                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    it.data?.let { data ->
                        val incomingParcelSocketArrayList1 =
                            sharedViewModel.incomingParcelSocketArrayList.filter {
                                it._id != id
                            }
                        sharedViewModel.incomingParcelSocketArrayList.clear()
                        sharedViewModel.incomingParcelSocketArrayList.addAll(
                            incomingParcelSocketArrayList1
                        )
                        isTrueParcel = true

//                        showToast(data.message)
                        sharedViewModel.incomingParcelAdapter?.notifyDataSetChanged()


                    }
                }

                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogHelperClass.errorDialog(requireContext(), it.message!!)
                }
            }
        })

    }

    override fun onRejectParcelClick(position: Int) {
        id = sharedViewModel.incomingParcelSocketArrayList[position]._id

        DialogHelperClass.submitReason(
            requireContext(), this, true, "", ""
        )

    }

    private val pushNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            Log.d("fcmToken", "granted")


        } else {
            Log.d("fcmToken", "granted else")
        }
    }

    override fun onSubmitoflineClick(status: String, rejectionReason: String) {

        RiderSocketClass.disconnetRider(rejectionReason)
        RiderSocketClass.disconnect()
        handler.removeCallbacks(runnable)
        sharedViewModel.incomingParcelSocketArrayList.clear()
        sharedViewModel.incomingOrderSocketArrayList.clear()
        mViewDataBinding.connnnn.visibility = View.GONE


        sharedViewModel.incomingParcelAdapter?.notifyDataSetChanged()
        sharedViewModel.incomingOrderAdapter?.notifyDataSetChanged()


//        val params = JsonObject()
//        try {
//            params.addProperty("offlineReason", rejectionReason)
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }

//        mViewModel.offlineReason(Activityid, params)

//        mViewModel.offlineReasonResponse.observe(requireActivity(), Observer {
//            when (it.status) {
//                Resource.Status.LOADING -> {
//                    loadingDialog.show()
//                }
//
//                Resource.Status.AUTH -> {
//                    loadingDialog.dismiss()
//                    onToSignUpPage()
//                }
//
//                Resource.Status.SUCCESS -> {
//                    loadingDialog.dismiss()
//                    it.data?.let { data ->
//                        RiderSocketClass.disconnect()
//                        dialog?.dismiss()
//
//                    }
//                }
//
//                Resource.Status.ERROR -> {
//                    loadingDialog.dismiss()
//                    DialogHelperClass.errorDialog(requireContext(), it.message!!)
//                }
//            }
//        })
    }

    override fun onCanceloflineClick() {
        Log.d("121212121", "onCanceloflineClick: 121212")

        seekBar1.progress = 100


    }

    override fun alloLocation() {
        requestPermission()
    }

    override fun denyLocation() {

    }


}