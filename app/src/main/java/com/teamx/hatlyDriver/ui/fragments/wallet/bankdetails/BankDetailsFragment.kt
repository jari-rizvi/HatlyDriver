package com.teamx.hatlyDriver.ui.fragments.wallet.bankdetails

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.navigation.navOptions
import com.google.gson.JsonObject
import com.teamx.hatlyDriver.BR
import com.teamx.hatlyDriver.MainApplication
import com.teamx.hatlyDriver.R
import com.teamx.hatlyDriver.baseclasses.BaseFragment
import com.teamx.hatlyDriver.data.remote.Resource
import com.teamx.hatlyDriver.databinding.FragmentBankDetailsBinding
import com.teamx.hatlyDriver.localization.LocaleManager
import com.teamx.hatlyDriver.ui.fragments.wallet.WalletViewModel
import com.teamx.hatlyDriver.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException


@AndroidEntryPoint
class BankDetailsFragment : BaseFragment<FragmentBankDetailsBinding, WalletViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_bank_details
    override val viewModel: Class<WalletViewModel>
        get() = WalletViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private var bankName: String? = null
    private var holderName: String? = null
    private var accNumber: String? = null


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

        if (!MainApplication.localeManager!!.getLanguage()
                .equals(LocaleManager.Companion.LANGUAGE_ENGLISH)
        ) {

            mViewDataBinding.imgBack.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.stripe_ic_arrow_right_circle,
                    requireActivity().theme
                )
            )

        } else {
            mViewDataBinding.imgBack.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.back_arrow,
                    requireActivity().theme
                )
            )

        }

        mViewDataBinding.imgBack.setOnClickListener {
            popUpStack()
        }
        mViewDataBinding.btnConfirm .setOnClickListener {
            isValidate()
        }
        mViewModel.me()
        if (!mViewModel.meResponse.hasActiveObservers()) {
            mViewModel.meResponse.observe(requireActivity()) {
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
                                mViewDataBinding.etacc.setText(data.bankDetail.accountNumber)
                                mViewDataBinding.etBankName.setText(data.bankDetail.bankName)
                                mViewDataBinding.etaccHolder.setText(data.bankDetail.accountHolderName)

                            } catch (e: Exception) {
                                ""
                            }


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

    private fun initialization() {
        bankName = mViewDataBinding.etBankName.text.toString().trim()
        holderName = mViewDataBinding.etaccHolder.text.toString().trim()
        accNumber = mViewDataBinding.etacc.text.toString().trim()
    }

    fun apiforBank() {

        initialization()

        if (!bankName!!.isEmpty() || !holderName!!.isEmpty() || !accNumber!!.isEmpty()) {

            val params = JsonObject()
            try {
                params.addProperty("bankName", bankName)
                params.addProperty("accountHolderName", holderName)
                params.addProperty("accountNumber", accNumber)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            mViewModel.addBankDetails(params)

            if (!mViewModel.bankDetailsResponse.hasActiveObservers()) {
                mViewModel.bankDetailsResponse.observe(requireActivity(), Observer {
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
                                mViewDataBinding.root.snackbar("Details Added Successfully!")
                                popUpStack()

                            }
                        }


                        Resource.Status.ERROR -> {
                            loadingDialog.dismiss()
                            mViewDataBinding.root.snackbar(it.message!!)
                        }
                    }
                })
            }


        }
    }

    fun isValidate(): Boolean {
        if (mViewDataBinding.etBankName.text.toString().trim().isEmpty()) {
            mViewDataBinding.root.snackbar("Enter Bank Name")
            return false
        }

        if (mViewDataBinding.etacc.text.toString().trim().isEmpty()) {
            mViewDataBinding.root.snackbar("Enter Your Account")
            return false
        }
        if (mViewDataBinding.etaccHolder.text.toString().trim().isEmpty()) {
            mViewDataBinding.root.snackbar("Enter Account Holder Name")
            return false
        }

        apiforBank()
        return true
    }

}