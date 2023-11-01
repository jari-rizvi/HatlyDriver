package com.teamx.hatly.ui.fragments.Dashboard.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
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
import com.teamx.hatly.BR
import com.teamx.hatly.R
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.data.dataclasses.getorders.PastDispatche
import com.teamx.hatly.data.remote.Resource
import com.teamx.hatly.databinding.FragmentHomeBinding
import com.teamx.hatly.ui.fragments.chat.socket.IncomingOrderCallBack
import com.teamx.hatly.ui.fragments.chat.socket.RiderSocketClass
import com.teamx.hatly.ui.fragments.chat.socket.model.allmessageData.Doc
import com.teamx.hatly.ui.fragments.chat.socket.model.incomingOrderSocketData.IncomingOrderSocketData
import com.teamx.hatly.ui.fragments.chat.socket.model.incomingParcelSoocketData.IncomingParcelSocketData
import com.teamx.hatly.utils.DialogHelperClass
import com.teamx.hatly.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(),
    DialogHelperClass.Companion.ConfirmLocationDialog, IncomingOrderCallBack, onAcceptReject {

    override val layoutId: Int
        get() = com.teamx.hatly.R.layout.fragment_home
    override val viewModel: Class<HomeViewModel>
        get() = HomeViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private lateinit var options: NavOptions

    lateinit var productArrayList: ArrayList<String>
    lateinit var productArrayList1: ArrayList<String>
    private lateinit var seekBar: SeekBar
    private lateinit var statusText: TextView

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var locationRequest: LocationRequest? = null


    private var originLatitude: String = "0.0"
    private var originLongitude: String = "0.0"


    //    lateinit var orderAdapter: AllOrdersAdapter
    lateinit var orderArrayList: ArrayList<Doc>

    lateinit var pastparcelArrayList: ArrayList<PastDispatche>
    lateinit var pastOrderArrayList: ArrayList<PastDispatche>
    lateinit var pastOrderAdapter: PastParcelAdapter


    var type: String = ""


    lateinit var incomingOrderAdapter: IncomingOrderSocketAdapter
    lateinit var incomingParcelAdapter: IncomingParcelSocketAdapter
    lateinit var incomingOrderSocketArrayList: ArrayList<IncomingOrderSocketData>
    lateinit var incomingParcelSocketArrayList: ArrayList<IncomingParcelSocketData>

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.lifecycleOwner = viewLifecycleOwner

        options = navOptions {
            anim {
                enter = com.teamx.hatly.R.anim.enter_from_left
                exit = com.teamx.hatly.R.anim.exit_to_left
                popEnter = com.teamx.hatly.R.anim.nav_default_pop_enter_anim
                popExit = com.teamx.hatly.R.anim.nav_default_pop_exit_anim
            }
        }

        mViewDataBinding.profilePicture.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.orderFragment, null, options)
        }
        mViewDataBinding.constraintLayout1.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.notificaitonFragment, null, options)
        }


        getDeviceLocation()


        /*      when (type) {
                  "parcel" -> {

                  }

                  "order" -> {

                  }
              }*/

        mViewModel.getOrders("order")
        mViewModel.getOrders("parcel")

        if (!mViewModel.getOrdersResponse.hasActiveObservers()) {
            mViewModel.getOrdersResponse.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            data.pastDispatches.forEach {
                                pastOrderArrayList.add(it)
                                pastparcelArrayList.add(it)
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
                if (isAdded) {
                    mViewModel.getOrdersResponse.removeObservers(
                        viewLifecycleOwner
                    )
                }
            }
        }




        Firebase.initialize(requireContext())
        FirebaseApp.initializeApp(requireContext())
        if (!mViewModel.fcmResponse.hasActiveObservers()) {
            askNotificationPermission()
        }

        if (!mViewModel.fcmResponse.hasActiveObservers()) {
            mViewModel.fcmResponse.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            mViewDataBinding.root.snackbar(data.message)
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
//       mViewDataBinding.slideToUnlock.externalListener = this
//        OrderRecyclerview()
//        incomingOrderArrayList = ArrayList()

        /*        productArrayList.add("")
                productArrayList.add("")
                productArrayList.add("")
                productArrayList.add("")

                productAdapter.notifyDataSetChanged()


                productArrayList1.add("")
                productArrayList1.add("")
                productArrayList1.add("")
                productArrayList1.add("")

                productAdapter.notifyDataSetChanged()
                */


        seekBar = mViewDataBinding.slider
        statusText = mViewDataBinding.statusText


        seekBar.isClickable = false

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                seekBar.isEnabled = true
                // Check if the SeekBar is fully swiped
                if (progress == seekBar.max) {

                    DialogHelperClass.confirmLocation(
                        requireContext(), this@HomeFragment, true
                    )

                    seekBar.thumb = resources.getDrawable(R.drawable.custom_thumb, null)
                    statusText.text = "Go Offline"

                } else {
                    // Reset thumb color to the default
                    seekBar.thumb = resources.getDrawable(R.drawable.custom_thumb, null)
                    RiderSocketClass.disconnect()
                    // Hide "Go Online" text
                    statusText.text = "Go Online"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Not needed for this example
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Not needed for this example
            }
        })


    }


    private fun OrderRecyclerview() {
        incomingOrderSocketArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        mViewDataBinding.recyclerViewIncomingOrders.layoutManager = linearLayoutManager

        incomingOrderAdapter = IncomingOrderSocketAdapter(incomingOrderSocketArrayList, this)
        mViewDataBinding.recyclerViewIncomingOrders.adapter = incomingOrderAdapter

    }

    private fun ParcelRecyclerview() {
        incomingParcelSocketArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        mViewDataBinding.recyclerViewSpecialOrders.layoutManager = linearLayoutManager

        incomingParcelAdapter = IncomingParcelSocketAdapter(incomingParcelSocketArrayList)
        mViewDataBinding.recyclerViewSpecialOrders.adapter = incomingParcelAdapter

    }


    private fun PastParcelRecyclerview() {
        pastparcelArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        mViewDataBinding.recyclerViewSpecialPastOrders.layoutManager = linearLayoutManager

        pastOrderAdapter = PastParcelAdapter(pastparcelArrayList)
        mViewDataBinding.recyclerViewSpecialPastOrders.adapter = pastOrderAdapter

    }


    private fun PastOrderRecyclerview() {
        pastOrderArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        mViewDataBinding.recyclerViewPastOrders.layoutManager = linearLayoutManager

        pastOrderAdapter = PastParcelAdapter(pastOrderArrayList)
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
//                            mapFragment!!.getMapAsync {
//                                mMap = it
//                                it!!.addMarker(
//                                    MarkerOptions().position(
//                                        LatLng(
//                                            originLatitude, originLongitude
//                                        )
//                                    )
//                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_png_pin)) // Specifies the anchor to be at a particular point in the marker image.
//                                        .anchor(0.5f, 1f)
//                                )
//                                it?.moveCamera(
//                                    CameraUpdateFactory.newLatLngZoom(
//                                        LatLng(
//                                            lastKnownLocation.latitude, lastKnownLocation.longitude
//                                        ), 1.toFloat()
//                                    )
//                                )
//                            }

                        }
                    } else {
                        Timber.tag("TAG").d("Current location is null. Using defaults.")
                        Timber.tag("TAG").d("Exception:   ${task.exception}")
//                        mMap?.moveCamera(
//                            CameraUpdateFactory.newLatLngZoom(
//                                LatLng(
//                                    originLatitude, originLongitude
//                                ), 1.toFloat()
//                            )
//                        )
                        mMap?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }


        } catch (e: SecurityException) {
            Timber.tag("Exception: %s").e(e, e.message)
        }
    }


//    private fun productRecyclerview1() {
//        productArrayList1 = ArrayList()
//
//        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
//        mViewDataBinding.recyclerView2.layoutManager = linearLayoutManager
//
//        productAdapter = IncomingAdapter(productArrayList1)
//        mViewDataBinding.recyclerView2.adapter = productAdapter
//
//    }

    override fun onConfirmLocation() {
        requestPermission()
        Log.d("121212121", "Click Chala: ")

    }


    private val PERMISSION_REQUEST_CODE = 123

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            /* RiderSocketClass.connectRider(
                 "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWNhdGlvbiI6eyJpdiI6IjZiNjQ3NTMzNjkzODM3NjM2ODMyNmIzOTM1MzczODY0IiwiZW5jcnlwdGVkRGF0YSI6IjM4OTFhZWVmYjBlZDgwZmU2ZDY3OWEwYWQzY2IzNGQyZWM3MDA4MDFjZWNiZDY0NDk4ZWZlOWEwZjMxMDNkMjEifSwidW5pcXVlSWQiOiI0OGZiMTU2OTg2ZDNkM2IzYmQ3ZTIyMjM0MmY0YTQiLCJpYXQiOjE2OTc0NzA4MzksImV4cCI6MTAzMzc0NzA4Mzl9.V-hG2OFgmRy8D0PQCICXNHp6GeqUpAXq09hqU8OXeco",
                 originLatitude,
                 originLongitude
             )*/

            // Show an explanation to the user *asynchronously*
            // why the permission is needed and why the user should grant it

            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission has already been granted
            RiderSocketClass.connectRider(
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWNhdGlvbiI6eyJpdiI6IjZiNjQ3NTMzNjkzODM3NjM2ODMyNmIzOTM1MzczODY0IiwiZW5jcnlwdGVkRGF0YSI6IjM4OTFhZWVmYjBlZDgwZmU2ZDY3OWEwYWQzY2IzNGQyZWM3MDA4MDFjZWNiZDY0NDk4ZWZlOWEwZjMxMDNkMjEifSwidW5pcXVlSWQiOiI0OGZiMTU2OTg2ZDNkM2IzYmQ3ZTIyMjM0MmY0YTQiLCJpYXQiOjE2OTc0NzA4MzksImV4cCI6MTAzMzc0NzA4Mzl9.V-hG2OFgmRy8D0PQCICXNHp6GeqUpAXq09hqU8OXeco",
                originLatitude,
                originLongitude,
                this
            )

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
            /*  navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
              navController.navigate(R.id.dashboard, null, options)*/

            RiderSocketClass.connectRider(
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWNhdGlvbiI6eyJpdiI6IjZiNjQ3NTMzNjkzODM3NjM2ODMyNmIzOTM1MzczODY0IiwiZW5jcnlwdGVkRGF0YSI6IjM4OTFhZWVmYjBlZDgwZmU2ZDY3OWEwYWQzY2IzNGQyZWM3MDA4MDFjZWNiZDY0NDk4ZWZlOWEwZjMxMDNkMjEifSwidW5pcXVlSWQiOiI0OGZiMTU2OTg2ZDNkM2IzYmQ3ZTIyMjM0MmY0YTQiLCJpYXQiOjE2OTc0NzA4MzksImV4cCI6MTAzMzc0NzA4Mzl9.V-hG2OFgmRy8D0PQCICXNHp6GeqUpAXq09hqU8OXeco",
                originLatitude,
                originLongitude, this
            )
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

            // Permission was denied. Handle this however is appropriate for your app.
        }
    }

    override fun onIncomingOrderRecieve(incomingOrderSocketData: IncomingOrderSocketData) {
        incomingOrderSocketArrayList.clear()
        GlobalScope.launch(Dispatchers.Main) {

            incomingOrderSocketArrayList.add(incomingOrderSocketData)


            /*       getAllChatsModelX.forEach {
                       var str = ""
                       val timestamp = it.createdAt


       //                val pattern: Pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})T(\\d{2}:\\d{2}:\\d{2})\\.\\d{3}Z")
       //                val matcher: Matcher = pattern.matcher(timestamp)
       //
       //                if (matcher.matches()) { val date: String = matcher.group(1)
       //                    val time: String = matcher.group(2)
       //                    println("Date: $date")
       //                    println("Time: $time")
       //                    str = time
       //                }
                       if (it.messages.isNotEmpty()) {

                           val timeStamp = timeFormatter(it.messages[0].createdAt)

                           it.messages[0].createdAt = timeStamp


       //                messagesListArrayList.add()
                           it.color = long.get(Random().nextInt(long.size - 1))
                           messagesListArrayList.add(
                               it*//*  AllChatsModel(
                    "1",
                    "${it.order_detail.get(0).shop}",
                    "#${it.order_detail.get(0)._id}",
                    str
                )*//*
                    )
                }


            }*/

//            incomingOrderAdapter.notifyDataSetChanged()

            mViewDataBinding.recyclerViewIncomingOrders.adapter?.notifyDataSetChanged()
        }
    }

    override fun onIncomingParcelRecieve(incomingParcelSocketData: IncomingParcelSocketData) {
        incomingParcelSocketArrayList.clear()
        GlobalScope.launch(Dispatchers.Main) {

            incomingParcelSocketArrayList.add(incomingParcelSocketData)


            /*       getAllChatsModelX.forEach {
                       var str = ""
                       val timestamp = it.createdAt


       //                val pattern: Pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})T(\\d{2}:\\d{2}:\\d{2})\\.\\d{3}Z")
       //                val matcher: Matcher = pattern.matcher(timestamp)
       //
       //                if (matcher.matches()) { val date: String = matcher.group(1)
       //                    val time: String = matcher.group(2)
       //                    println("Date: $date")
       //                    println("Time: $time")
       //                    str = time
       //                }
                       if (it.messages.isNotEmpty()) {

                           val timeStamp = timeFormatter(it.messages[0].createdAt)

                           it.messages[0].createdAt = timeStamp


       //                messagesListArrayList.add()
                           it.color = long.get(Random().nextInt(long.size - 1))
                           messagesListArrayList.add(
                               it*//*  AllChatsModel(
                    "1",
                    "${it.order_detail.get(0).shop}",
                    "#${it.order_detail.get(0)._id}",
                    str
                )*//*
                    )
                }


            }*/

//            incomingOrderAdapter.notifyDataSetChanged()

            mViewDataBinding.recyclerViewSpecialOrders.adapter?.notifyDataSetChanged()

        }
    }

    override fun onAcceptClick(position: Int) {
    }

    override fun onRejectClick(position: Int) {
    }


    private fun askNotificationPermission() {
        Log.d("fcmToken", "askNotificationPermission")

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



}