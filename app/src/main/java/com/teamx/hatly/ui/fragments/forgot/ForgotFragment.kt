package com.teamx.hatly.ui.fragments.forgot

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.google.gson.JsonObject
import com.teamx.hatly.R
import com.teamx.hatly.BR
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.data.remote.Resource
import com.teamx.hatly.databinding.FragmentForgotBinding
import com.teamx.hatly.databinding.FragmentSignupBinding
import com.teamx.hatly.utils.DialogHelperClass
import com.teamx.hatly.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
@AndroidEntryPoint
class ForgotFragment : BaseFragment<FragmentForgotBinding, ForgotViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_forgot
    override val viewModel: Class<ForgotViewModel>
        get() = ForgotViewModel::class.java
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

            mViewDataBinding.btnSend.setOnClickListener {
                navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(R.id.otpFragment, null, options)
            }

       mViewDataBinding.btnBack.setOnClickListener {
           popUpStack()
       }

    }

}