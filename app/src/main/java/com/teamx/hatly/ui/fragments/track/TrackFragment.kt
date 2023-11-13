package com.teamx.hatly.ui.fragments.track

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.navigation.navOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.teamx.hatly.BR
import com.teamx.hatly.R
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.databinding.FragmentTrackBinding
import com.teamx.hatly.ui.activity.mainActivity.MainActivity
import com.teamx.hatly.ui.fragments.topUp.TopUpModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackFragment : BaseFragment<FragmentTrackBinding, TopUpModel>(), OnMapReadyCallback,android.location.LocationListener {

    override val layoutId: Int
        get() = R.layout.fragment_track
    override val viewModel: Class<TopUpModel>
        get() = TopUpModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    var mapFragment: SupportMapFragment? = null

    var locationCallback: LocationCallback? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>


    private lateinit var fusedLocationClient: FusedLocationProviderClient

    var locationRequest: LocationRequest? = null
    private var originLatitude2: Double = 24.9324
    private var originLongitude2: Double = 67.0873

    // Coordinates of a park nearby
    private var destinationLatitude: Double = 0.0
    private var destinationLongitude: Double = 0.0
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        options = navOptions {
            anim {
                enter = R.anim.enter_from_left
                exit = R.anim.exit_to_left
                popEnter = R.anim.nav_default_pop_enter_anim
                popExit = R.anim.nav_default_pop_exit_anim
            }
        }


        bottomSheetBehavior =
            BottomSheetBehavior.from(mViewDataBinding.bottomSheetLayout.bottomSheet)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> MainActivity.bottomNav?.visibility =
                        View.GONE

                    BottomSheetBehavior.STATE_COLLAPSED -> MainActivity.bottomNav?.visibility =
                        View.VISIBLE

                    else -> "Persistent Bottom Sheet"
                }
            }
        })

        val state =
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_COLLAPSED
            else BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.state = state





        mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            )

            Log.d("permissionIsAllowed", "onViewCreated: if")

//            requestFineLocationPermission()
//            requestBackgroundLocationPermission()
            return
        } else {

            if (!checkLocationIsEnable()) {
                Log.d("permissionIsAllowed", "isMyLocationButtonEnabled: if")
//                mMap?.isMyLocationEnabled = true
//                mMap?.uiSettings?.isMyLocationButtonEnabled = true
                Toast.makeText(requireActivity(), "Enable your location!", Toast.LENGTH_SHORT)
                    .show()
                return
            } else {
                getUserLocation()
            }

        }


    }


    private fun checkLocationIsEnable(): Boolean {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun getUserLocation() {
        locationRequest = LocationRequest.create()
        locationRequest?.interval = 100
        locationRequest?.fastestInterval = 50
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (isAdded) {
                    if (locationResult != null) {

                        //Showing the latitude, longitude and accuracy on the home screen.
                        for (location in locationResult.locations) {
                            Log.d("permissionIsAllowed", "locationResult: ${location}")
                            originLatitude2 = location.latitude
                            originLongitude2 = location.longitude

                            val desLocation = LatLng(originLatitude2, originLongitude2)

                          /*  mViewModel.carMarker?.remove()

                            if (mViewModel.carMarker == null && strETA.isNotBlank()) {
                                *//*  carMarker = mMap?.addMarker(
                              MarkerOptions().position(desLocation)
                                  .icon(BitmapDescriptorFactory.fromResource(R.drawable.carspic))
                          )*//*
                                if (isAdded) {
                                    mViewModel.carMarker =
                                        voisMap(CarMarkerModel("ETA: ", strETA), desLocation)
                                }
                            }

                            mViewModel.carMarker?.position = desLocation
//                        if (isAdded) {
//                            voisMap2(ShopModel("adfasf", "", desLocation, "$strETA"), desLocation)
//                        }

//                        mViewModel.carMarker?.remove()
                            mViewModel.carMarker = mMap?.addMarker(
                                MarkerOptions().position(desLocation)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.carspic))
                            )

                            s2.updateRide(orderId, originLatitude2, originLongitude2)

//                        Log.d("animateMarker", "carMarker: ${mViewModel.carMarker!!}")
//                        Log.d("animateMarker", "desLocation: ${desLocation}")


                            animateMarker(mViewModel.carMarker!!, desLocation, false)


                            mViewDataBinding.tvLoc.text = MessageFormat.format(
                                "Lat: {0} Long: {1} Accuracy: {2}",
                                location.latitude,
                                location.longitude,
                                location.accuracy
                            )
*/
//                            setNotifyValues()
                            if (ActivityCompat.checkSelfPermission(
                                    requireContext(),
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {

                                ActivityCompat.requestPermissions(
                                    requireActivity(),
                                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                                    0
                                )
                                return
                            }
//                            notificationManager?.notify(1, builder!!.build())


//                        mMap!!.addMarker(MarkerOptions().position(desLocation))
                            val destinationLocation =
                                LatLng(destinationLatitude, destinationLongitude)

                        /*    if (isAdded) {
                                Log.d("strETA", "strETA: ${strETA}")
                                voisMap2(
                                    ShopModel(sharedViewModel.trackShopsDetail.value!!.shopName, sharedViewModel.trackShopsDetail.value!!.shopImg, destinationLocation, strETA), destinationLocation
                                )
                                mViewDataBinding.textView23.text = strETA
                                mViewDataBinding.textView21.text = totalDistanceTrack
                            }
//                        sharedViewModel.trackShopsDetail.value.shopName
//
                            val urll = getDirectionURL(desLocation, destinationLocation, apiKey)
                            try {

                                GetDirection(urll).execute()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }*/
//                        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(desLocation, 17F))
                        }
                    }
                }
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (isAdded) {
                originLatitude2 = it.latitude
                originLongitude2 = it.longitude
                val originLocation = LatLng(originLatitude2, originLongitude2)
                isMarkerRemove = true
               /* mViewModel.carMarker = mMap?.addMarker(
                    MarkerOptions().position(originLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.carspic))
                )*/

                val destinationLocation = LatLng(destinationLatitude, destinationLongitude)
               /* if (isAdded) {
                    Log.d("strETA", "strETA: ${strETA}")
                    voisMap2(
                        ShopModel(
                            sharedViewModel.trackShopsDetail.value!!.shopName,
                            sharedViewModel.trackShopsDetail.value!!.shopImg,
                            destinationLocation,
                            strETA
                        ), destinationLocation
                    )
                }
                val desLocation = LatLng(originLatitude2, originLongitude2)
                val urll = getDirectionURL(desLocation, destinationLocation, apiKey)
                try {
                    GetDirection(urll).execute()
                } catch (e: Exception) {
                    e.printStackTrace()
                }*/

//                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 17F))
//                goNow()
                handler.postDelayed(runnable, 5000)
                Log.d("lastLocation", "onCreate:latitude${it.latitude}longitude${it.longitude} ")
            }
        }

    }

    private val handler = Handler()

    private val runnable = kotlinx.coroutines.Runnable {
        Log.d("runnableIs", "Runnable: ")
        reqLocation()
    }

    var isMarkerRemove = true
    private fun reqLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
       /* if (isMarkerRemove) {
            mViewModel.carMarker?.remove()
            isMarkerRemove = false
        }*/
        fusedLocationClient.removeLocationUpdates(locationCallback!!)
        fusedLocationClient.requestLocationUpdates(
            locationRequest!!, locationCallback!!, Looper.getMainLooper()
        )
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 5000)
//        handler.post(runnable)
    }



    private val PERMISSION_REQUEST_CODE = 123

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
//            navController.navigate(R.id.discoverFragment, null, options)
          /*  DialogHelperClass.startRide(
                requireContext(), this, true
            )*/
        } else {
            val snackbar = Snackbar.make(
                mViewDataBinding.root,
                "Permission required to proceed..",
                Snackbar.LENGTH_SHORT
            )
            snackbar.setAction("Settings") {
                //
                Log.d("TAG", "ScankonRequestPermissionsResult: ")
                /*        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
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


    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                        // Precise location access granted.
                        Log.d("allowLocation", "locationPermissionRequest: ACCESS_FINE_LOCATION")
                      /*  DialogHelperClass.startRide(
                            requireContext(), this, true
                        )*/
                    }

                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                        // Only approximate location access granted.
                        Log.d("allowLocation", "locationPermissionRequest: ACCESS_COARSE_LOCATION")
                       /* DialogHelperClass.startRide(
                            requireContext(), this, true
                        )*/
                    }

                    permissions.getOrDefault(
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION, false
                    ) -> {
                        Log.d(
                            "allowLocation",
                            "locationPermissionRequest: ACCESS_BACKGROUND_LOCATION"
                        )
                    }

                    else -> {
                        // No location access granted.
                        Log.d("allowLocation", "locationPermissionRequest: else")
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", context?.packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                }
            } else {
                Log.d("allowLocation", "locationPermissionRequest: not working")
            }
        }


    override fun onMapReady(p0: GoogleMap) {
    }

    override fun onLocationChanged(p0: Location) {
    }


}