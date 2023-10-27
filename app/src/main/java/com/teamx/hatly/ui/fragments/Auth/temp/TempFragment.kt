package com.teamx.hatly.ui.fragments.Auth.temp

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController

import androidx.navigation.navOptions
import com.teamx.hatly.BR
import com.teamx.hatly.R
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.constants.NetworkCallPoints.Companion.TOKENER
import com.teamx.hatly.databinding.FragmentTempBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

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

        options = navOptions {
            anim {
                enter = R.anim.enter_from_left
                exit = R.anim.exit_to_left
                popEnter = R.anim.nav_default_pop_enter_anim
                popExit = R.anim.nav_default_pop_exit_anim
            }
        }


        CoroutineScope(Dispatchers.Main).launch {

            dataStoreProvider.token.collect {
                Timber.tag("TAG").d("CoroutineScope ${it}")

                val token = it

                TOKENER = token.toString()

                if (token.isNullOrBlank()) {

                    mViewDataBinding.login.setOnClickListener {
                        loginListener()
                    }
                    mViewDataBinding.register.setOnClickListener {
                        registerListener()
                    }
                } else {
                    findNavController().navigate(R.id.action_tempFragment_to_homeFragment)

                }
            }

        }


//        Handler(Looper.getMainLooper()).postDelayed({
//            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
//            navController.navigate(R.id.logInFragment, null, options)
//
//        }, 2000)


    }

    private fun loginListener() {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        navController.navigate(R.id.logInFragment, null, options)
    }

    private fun registerListener() {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        navController.navigate(R.id.signupFragment, null, options)
    }
}
