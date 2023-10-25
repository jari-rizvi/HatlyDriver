package com.teamx.hatly.ui.fragments.Dashboard.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.teamx.hatly.BR
import com.teamx.hatly.R
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.data.dataclasses.getorders.Doc
import com.teamx.hatly.data.remote.Resource
import com.teamx.hatly.databinding.FragmentHomeBinding
import com.teamx.hatly.ui.fragments.chat.socket.RiderSocketClass
import com.teamx.hatly.ui.fragments.orders.Incoming.IncomingAdapter
import com.teamx.hatly.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(),  DialogHelperClass.Companion.ConfirmLocationDialog {

    override val layoutId: Int
        get() = com.teamx.hatly.R.layout.fragment_home
    override val viewModel: Class<HomeViewModel>
        get() = HomeViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private lateinit var options: NavOptions
    lateinit var productAdapter: IncomingAdapter

    lateinit var productArrayList: ArrayList<String>
    lateinit var productArrayList1: ArrayList<String>
    private lateinit var seekBar: SeekBar
    private lateinit var statusText: TextView

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var locationRequest: LocationRequest? = null


    private var originLatitude: String = "0.0"
    private var originLongitude: String = "0.0"


    lateinit var orderAdapter: AllOrdersAdapter
    lateinit var orderArrayList: ArrayList<Doc>

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


//        mViewModel.getOrders("1000", "1", "CASH_ON_DELIVERY")

        if (!mViewModel.getOrdersResponse.hasActiveObservers()) {
            mViewModel.getOrdersResponse.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            data.docs.forEach {
                                orderArrayList.add(it)
                            }

                            orderAdapter.notifyDataSetChanged()


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


//       mViewDataBinding.slideToUnlock.externalListener = this
        OrderRecyclerview()
        productRecyclerview1()

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
        seekBar.isFocusable = false

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // Check if the SeekBar is fully swiped
                if (progress == seekBar.max) {
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        DialogHelperClass.confirmLocation(
                            requireContext(), this@HomeFragment, true
                        )

                    } else {

                    }
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
    var locationPermissionGranted = true


    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                requireActivity().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 111
            )
        }
    }
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
                       /*     mapFragment!!.getMapAsync {
                                mMap = it
                                it!!.addMarker(
                                    MarkerOptions().position(
                                        LatLng(
                                            originLatitude, originLongitude
                                        )
                                    )
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_png_pin)) // Specifies the anchor to be at a particular point in the marker image.
                                        .anchor(0.5f, 1f)
                                )
                                it?.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(
                                            lastKnownLocation.latitude, lastKnownLocation.longitude
                                        ), 1.toFloat()
                                    )
                                )
                            }*/

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



    private fun OrderRecyclerview() {
        orderArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        mViewDataBinding.recyclerViewPastOrder.layoutManager = linearLayoutManager

        orderAdapter = AllOrdersAdapter(orderArrayList)
        mViewDataBinding.recyclerViewPastOrder.adapter = orderAdapter

    }

    private fun productRecyclerview1() {
        productArrayList1 = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        mViewDataBinding.recyclerView2.layoutManager = linearLayoutManager

        productAdapter = IncomingAdapter(productArrayList1)
        mViewDataBinding.recyclerView2.adapter = productAdapter

    }

    override fun onConfirmLocation() {
        Log.d("121212121", "Click Chala: ")
        RiderSocketClass.connectRider(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWNhdGlvbiI6eyJpdiI6IjZiNjQ3NTMzNjkzODM3NjM2ODMyNmIzOTM1MzczODY0IiwiZW5jcnlwdGVkRGF0YSI6IjM4OTFhZWVmYjBlZDgwZmU2ZDY3OWEwYWQzY2IzNGQyZWM3MDA4MDFjZWNiZDY0NDk4ZWZlOWEwZjMxMDNkMjEifSwidW5pcXVlSWQiOiI0OGZiMTU2OTg2ZDNkM2IzYmQ3ZTIyMjM0MmY0YTQiLCJpYXQiOjE2OTc0NzA4MzksImV4cCI6MTAzMzc0NzA4Mzl9.V-hG2OFgmRy8D0PQCICXNHp6GeqUpAXq09hqU8OXeco",
            originLatitude,
            originLongitude
        )
    }


}