package com.teamx.hatly.ui.fragments.temp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.asLiveData
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.teamx.hatly.R
import com.teamx.hatly.BR
import com.teamx.hatly.baseclasses.BaseActivity
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.constants.NetworkCallPoints.Companion.TOKENER
import com.teamx.hatly.databinding.FragmentTempBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TempFragment : BaseFragment<FragmentTempBinding, TempViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_temp
    override val viewModel: Class<TempViewModel>
        get() = TempViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private lateinit var options: NavOptions


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.lifecycleOwner = viewLifecycleOwner

        options = navOptions {
            anim {
                enter = R.anim.enter_from_left
                exit = R.anim.exit_to_left
                popEnter = R.anim.nav_default_pop_enter_anim
                popExit = R.anim.nav_default_pop_exit_anim
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if (isAdded) {

           /*     dataStoreProvider.token.asLiveData().observe(
                    requireActivity()
                ) {
                    val token = it
                    Log.d("Databsae Token ", token.toString())
                    Log.d("Databsae Token ", token.toString())
                    *//*NetworkCallPointsNest.*//*TOKENER = token.toString()

                    if (token == null) {
                        navController =
                            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        navController.navigate(R.id.introFragment, null, options)


                    } else {
                        if (isAdded) {

                            if (ActivityCompat.checkSelfPermission(
                                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                navController = Navigation.findNavController(
                                    requireActivity(), R.id.nav_host_fragment
                                )
                                navController.navigate(R.id.allowLocationFragment, null, options)
                            } else {
                                navController = Navigation.findNavController(
                                    requireActivity(), R.id.nav_host_fragment
                                )
                                navController.navigate(R.id.dashboard, null, options)
                            }


                        }
                    }
                }
*/
            }

        }, 2000)

//
//        Handler(Looper.getMainLooper()).postDelayed({
//            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
//            navController.navigate(R.id.introFragment, null,options)
//
//        }, 2000)


        clickListener()
    }

    var allowPermissionListener: BaseActivity.OnAllowPermissionListener? = null
    private val SOME_PERMISSION = 0
    private fun clickListener() {

    }

}