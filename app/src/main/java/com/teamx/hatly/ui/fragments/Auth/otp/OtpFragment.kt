package com.teamx.hatly.ui.fragments.Auth.otp

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.google.gson.JsonObject
import com.teamx.hatly.BR
import com.teamx.hatly.R
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.data.remote.Resource
import com.teamx.hatly.databinding.FragmentOtpBinding
import com.teamx.hatly.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException

@AndroidEntryPoint
class OtpFragment : BaseFragment<FragmentOtpBinding, OtpViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_otp
    override val viewModel: Class<OtpViewModel>
        get() = OtpViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private lateinit var options: NavOptions
    private var phoneNumber: String? = null
    private var sid: String? = null
    private var otpid: String? = null

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


        mViewDataBinding.btnBack.setOnClickListener {
            popUpStack()
        }

        mViewDataBinding.btnSend.setOnClickListener {

            verifyotp()
        }

        mViewDataBinding.btnResend.isEnabled = false
        mViewDataBinding.btnResend.setOnClickListener {
            resendOtp()

        }

        object : CountDownTimer(60000, 1000) {

            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {

                mViewDataBinding.timer.text =
                    "Resend otp in 00:" + millisUntilFinished / 1000 + " sec"

            }

            // Callback function, fired
            // when the time is up
            override fun onFinish() {
                mViewDataBinding.textView.text = "done!"
                mViewDataBinding.btnResend.isEnabled = true
                mViewDataBinding.btnSend.visibility = View.GONE
            }
        }.start()

        initialization()

    }

    private fun initialization() {
        val bundle = arguments
        if (bundle != null) {
            phoneNumber = bundle.getString("phone").toString()
            sid = bundle.getString("Sid").toString()
            otpid = bundle.getString("otpid")

            mViewDataBinding.txtPhoneNumber.text = phoneNumber

        }
    }

    private fun verifyotp() {


        val code = mViewDataBinding.pinView.text.toString()
        if (sid!!.isNotEmpty() || otpid!!.isNotEmpty() || phoneNumber!!.isNotEmpty()) {
            val params = JsonObject()
            try {
                params.addProperty("sid", sid.toString())
                params.addProperty("otp_id", otpid.toString())
                params.addProperty("phone_number", phoneNumber.toString())
                params.addProperty("code", code)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            mViewModel.otpVerify(params)

            mViewModel.otpVerifyResponse.observe(requireActivity(), Observer {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->

                            navController = Navigation.findNavController(
                                requireActivity(),
                                R.id.nav_host_fragment
                            )
                            navController.navigate(R.id.homeFragment, null, options)

                        }
                    }
                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(requireContext(), it.message!!)
                    }
                }
            })


        }
    }

    private fun resendOtp() {

        if (phoneNumber!!.isNotEmpty()) {
            val params = JsonObject()
            try {
                params.addProperty("contact", phoneNumber)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            mViewDataBinding.btnSend.isEnabled = false
            mViewDataBinding.btnResend.visibility = View.GONE
            mViewDataBinding.btnSend.visibility = View.VISIBLE



            mViewModel.resendOtp(params)

            mViewModel.resendOtpResponse.observe(requireActivity(), Observer {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            showToast(data.message)
                            mViewDataBinding.btnSend.isEnabled = true

                        }
                    }
                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(requireContext(), it.message!!)
                    }
                }
            })


        }
    }
}