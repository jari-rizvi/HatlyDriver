package com.teamx.hatly.ui.fragments.Auth.login

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.teamx.hatly.R
import com.teamx.hatly.BR
import com.teamx.hatly.baseclasses.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment :
    BaseFragment<com.teamx.hatly.databinding.FragmentLoginBinding, LoginViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_login
    override val viewModel: Class<LoginViewModel>
        get() = LoginViewModel::class.java
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

        mViewDataBinding.btnLogin.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.action_logInFragment_to_homeFragment, null, options)
        }
        mViewDataBinding.btnSingup.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.action_logInFragment_to_signupFragment, null, options)
        }

         mViewDataBinding.forgetPass.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.forgotFragment, null, options)
        }



        mViewDataBinding.btnBack.setOnClickListener {
            popUpStack()
        }


    }
}