package com.teamx.hatly.ui.fragments.Dashboard.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import com.teamx.hatly.BR
import com.teamx.hatly.R
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.data.dataclasses.getOrderStatus.Doc
import com.teamx.hatly.data.remote.Resource
import com.teamx.hatly.databinding.FragmentHomeBinding
import com.teamx.hatly.ui.fragments.chat.socket.IncomingOrderCallBack
import com.teamx.hatly.ui.fragments.chat.socket.RiderSocketClass
import com.teamx.hatly.ui.fragments.chat.socket.model.incomingOrderSocketData.IncomingOrdersSocketData
import com.teamx.hatly.ui.fragments.chat.socket.model.incomingParcelSoocketData.IncomingParcelSocketData
import com.teamx.hatly.ui.fragments.orders.Incoming.onAcceptReject
import com.teamx.hatly.utils.DialogHelperClass
import com.teamx.hatly.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(),
    DialogHelperClass.Companion.ReasonDialog,
    onAcceptReject, DialogHelperClass.Companion.ConfirmLocationDialog, IncomingOrderCallBack,
    onAcceptRejectSocket, onAcceptRejectParcel {

    override val layoutId: Int
        get() = R.layout.fragment_home
    override val viewModel: Class<HomeViewModel>
        get() = HomeViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var id: String

    private lateinit var seekBar: SeekBar
    private lateinit var statusText: TextView

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var locationRequest: LocationRequest? = null


    private var originLatitude: String = "0.0"
    private var originLongitude: String = "0.0"


    lateinit var pastparcelArrayList: ArrayList<com.teamx.hatly.data.dataclasses.pastParcels.Doc>
    lateinit var pastOrderArrayList: ArrayList<com.teamx.hatly.data.dataclasses.pastorder.Doc>

    lateinit var pastOrderAdapter: PastOrderAdapter
    lateinit var pastParcelAdapter: PastParcelAdapter


    var type: String = ""
    var userimg: String = ""
    var username: String = ""

    lateinit var incomingOrderAdapter: IncomingOrderSocketAdapter
    lateinit var incomingParcelAdapter: IncomingParcelSocketAdapter
    lateinit var incomingOrderSocketArrayList: ArrayList<com.teamx.hatly.ui.fragments.chat.socket.model.incomingOrderSocketData.Doc>
    lateinit var incomingParcelSocketArrayList: ArrayList<com.teamx.hatly.ui.fragments.chat.socket.model.incomingParcelSoocketData.Doc>

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


        try {

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
        }
        catch (e:Exception){}

        apiCalls()


        mViewDataBinding.userProfile.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.editProfileFragment, arguments, options)
        }

        mViewDataBinding.constraintLayout1.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.notificaitonFragment, arguments, options)
        }
        mViewDataBinding.constraintLayout.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.profileFragment, arguments, options)
        }

        mViewDataBinding.btnPastParcelAll.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.parcelFragment, arguments, options)
        }

        mViewDataBinding.textView18.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.parcelFragment, arguments, options)
        }

        mViewDataBinding.btnPastOrderAll.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.orderFragment, arguments, options)
        }

        mViewDataBinding.textView20.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.orderFragment, arguments, options)
        }


        getDeviceLocation()

        Firebase.initialize(requireContext())
        FirebaseApp.initializeApp(requireContext())

        if (!mViewModel.fcmResponse.hasActiveObservers()) {
            getFcmToken()
        }



        OrderRecyclerview()
        ParcelRecyclerview()
        PastOrderRecyclerview()
        PastParcelRecyclerview()


        seekBar = mViewDataBinding.slider
        statusText = mViewDataBinding.statusText


        seekBar.isClickable = false

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                seekBar.isEnabled = true
                var previousProgress = 0

                /*
                                if (progress > previousProgress) {
                                    // If progressing to the right, set the progress to the maximum value
                                    seekBar.progress = seekBar.max

                                    DialogHelperClass.confirmLocation(
                                        requireContext(), this@HomeFragment, true
                                    )

                                    seekBar.thumb = resources.getDrawable(R.drawable.custom_thumb, null)
                                    statusText.text = "Go Offline"
                                } else if (progress < previousProgress) {
                                    seekBar.progress = seekBar.min
                                    seekBar.thumb = resources.getDrawable(R.drawable.custom_thumb, null)
                                    RiderSocketClass.disconnect()
                                    // Hide "Go Online" text
                                    statusText.text = "Go Online"
                                }
                                previousProgress = progress // Update the previous progress valu
                */


                // Check if the SeekBar is fully swiped
                if (progress == seekBar.max) {

                    DialogHelperClass.confirmLocation(
                        requireContext(), this@HomeFragment, true
                    )

                    seekBar.thumb = resources.getDrawable(R.drawable.custom_thumb, null)
                    statusText.text = "Go Offline"
                    Log.d("TAG", "onProgressChanged11: ")
                    Log.d("TAG", "Online: ")


                } else {
                    // Reset thumb color to the default
                    seekBar.thumb = resources.getDrawable(R.drawable.custom_thumb, null)
                    Log.d("TAG", "Offilne: ")
                    Log.d("TAG", "onProgressChanged12: ")


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


        val spinner = mViewDataBinding.spinner
        val spinner1 = mViewDataBinding.spinner1

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_items,
            android.R.layout.simple_spinner_item
        )

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        spinner.adapter = adapter
        spinner1.adapter = adapter

        // Set a selection listener to handle item selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                    val selectedItem = parent.getItemAtPosition(position) as String
                    mViewModel.getTotalEarnings(selectedItem, "earning")


            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

            spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = parent.getItemAtPosition(position) as String

                    mViewModel.getTotalEarnings(selectedItem, "order")


                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
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

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            mViewDataBinding.totalEarnings.text = data.totalEarning.toString()
                            mViewDataBinding.totalorders.text = data.totalOrder.toString()
                            mViewDataBinding.totalParcels.text = data.totalParcels.toString()

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

        incomingParcelAdapter = IncomingParcelSocketAdapter(incomingParcelSocketArrayList, this)
        mViewDataBinding.recyclerViewSpecialOrders.adapter = incomingParcelAdapter

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
        requestPermission()


    }


    private val PERMISSION_REQUEST_CODE = 123

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

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

    override fun onIncomingOrderRecieve(incomingOrderSocketData: com.teamx.hatly.ui.fragments.chat.socket.model.incomingOrderSocketData.Doc) {
        /*    incomingOrderSocketArrayList.clear()*/

        Log.d("TAG", "onIncomingOrderRecieveSinglre:")
        GlobalScope.launch(Dispatchers.Main) {
            Log.d("TAG", "onIncomingOrderRecieveSinglre:")

            incomingOrderSocketArrayList.add(0, incomingOrderSocketData)


            mViewDataBinding.recyclerViewIncomingOrders.adapter?.notifyDataSetChanged()
        }
    }

    override fun onGetAllOrderRecieve(incomingOrderSocketData: IncomingOrdersSocketData) {
//        incomingOrderSocketArrayList.clear()
        Log.d("TAG", "onIncomingOrderRecieveAll:")

        GlobalScope.launch(Dispatchers.Main) {

            incomingOrderSocketArrayList.addAll(incomingOrderSocketData.docs)
            Log.d("TAG", "onIncomingOrderRecieveAll:")



            mViewDataBinding.recyclerViewIncomingOrders.adapter?.notifyDataSetChanged()
        }

    }

    override fun onGetAllParcelRecieve(incomingParcelSocketData: IncomingParcelSocketData) {
        incomingParcelSocketArrayList.clear()
        GlobalScope.launch(Dispatchers.Main) {

            incomingParcelSocketArrayList.addAll(incomingParcelSocketData.docs)


            mViewDataBinding.recyclerViewSpecialOrders.adapter?.notifyDataSetChanged()

        }
    }


    override fun onIncomingParcelRecieve(incomingParcelSocketData: com.teamx.hatly.ui.fragments.chat.socket.model.incomingParcelSoocketData.Doc) {
        incomingParcelSocketArrayList.clear()
        GlobalScope.launch(Dispatchers.Main) {

            incomingParcelSocketArrayList.add(0, incomingParcelSocketData)


            mViewDataBinding.recyclerViewSpecialOrders.adapter?.notifyDataSetChanged()

        }
    }


    override fun onAcceptSokcetClick(position: Int) {
        id = incomingOrderSocketArrayList[position]._id

        Log.d("TAG", "onAcceptClick: $id")
        val params = JsonObject()
        try {
            params.addProperty("status", "accepted")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        mViewModel.acceptReject(id, params)

        mViewModel.acceptRejectResponse.observe(requireActivity(), Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }

                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    it.data?.let { data ->
                        val incomingOrderSocketArrayList1 = incomingOrderSocketArrayList.filter {
                            it._id != id
                        }
                        incomingOrderSocketArrayList.clear()
                        incomingOrderSocketArrayList.addAll(incomingOrderSocketArrayList1)

                        showToast(data.message)
                        navController =
                            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        navController.navigate(R.id.orderFragment, null, options)
                        incomingOrderAdapter.notifyDataSetChanged()
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
        id = incomingOrderSocketArrayList[position]._id

        DialogHelperClass.submitReason(
            requireContext(), this, true, "", ""
        )
    }

    override fun onSubmitClick(status: String, rejectionReason: String) {
        val params = JsonObject()
        try {
            params.addProperty("status", "rejected")
            params.addProperty("rejectionReason", rejectionReason)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        mViewModel.acceptReject(id, params)

        mViewModel.acceptRejectResponse.observe(requireActivity(), Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }

                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    it.data?.let { data ->
                        val incomingOrderSocketArrayList1 = incomingOrderSocketArrayList.filter {
                            it._id != id
                        }


                        incomingOrderSocketArrayList.removeLast()

//                        incomingOrderSocketArrayList.clear()
//                        incomingOrderSocketArrayList.addAll(incomingOrderSocketArrayList1)
                        showToast(data.message)
                        incomingOrderAdapter.notifyDataSetChanged()

                        navController =
                            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        navController.navigate(R.id.orderFragment, null, options)


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


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Firebase.initialize(requireContext())
            FirebaseApp.initializeApp(requireContext())
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("123123", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                val token = task.result
                val params = JsonObject()
                params.addProperty("fcmToken", token)

                mViewModel.fcm(params)
//                val msg = getString(R.string.about_us, token)
                Log.d("123123111", token.toString())
            })

        } else {
        }
    }

    private fun getFcmToken() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("123123", "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    val token = task.result
                    val params = JsonObject()
                    params.addProperty("fcmToken", token)

                    mViewModel.fcm(params)
                    Timber.tag("123123").d(token.toString())
                })
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {

            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


    override fun onAcceptClick(position: Int) {
    }

    override fun onRejectClick(position: Int) {
    }

    override fun onAcceptParcelClick(position: Int) {
        id = incomingParcelSocketArrayList[position]._id

        Log.d("TAG", "onAcceptClick: $id")
        val params = JsonObject()
        try {
            params.addProperty("status", "accepted")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        mViewModel.acceptReject(id, params)

        mViewModel.acceptRejectResponse.observe(requireActivity(), Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }

                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    it.data?.let { data ->
                        val incomingParcelSocketArrayList1 = incomingParcelSocketArrayList.filter {
                            it._id != id
                        }
                        incomingParcelSocketArrayList.clear()
                        incomingParcelSocketArrayList.addAll(incomingParcelSocketArrayList1)

                        showToast(data.message)
                        incomingParcelAdapter.notifyDataSetChanged()

                        navController =
                            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        navController.navigate(R.id.parcelFragment, null, options)
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
        id = incomingParcelSocketArrayList[position]._id

        DialogHelperClass.submitReason(
            requireContext(), this, true, "", ""
        )

    }

}