package com.teamx.hatlyDriver.ui.fragments.wallet.withdraw

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.navOptions
import com.google.gson.JsonObject
import com.teamx.hatlyDriver.BR
import com.teamx.hatlyDriver.MainApplication
import com.teamx.hatlyDriver.R
import com.teamx.hatlyDriver.baseclasses.BaseFragment
import com.teamx.hatlyDriver.data.remote.Resource
import com.teamx.hatlyDriver.databinding.FragmentWithDrawBinding
import com.teamx.hatlyDriver.localization.LocaleManager
import com.teamx.hatlyDriver.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WithDrawFragment : BaseFragment<FragmentWithDrawBinding, WithdrawalViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_with_draw
    override val viewModel: Class<WithdrawalViewModel>
        get() = WithdrawalViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    private var amount = ""


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
        mViewDataBinding.txtLogin.setOnClickListener {
            val params = JsonObject()

            amount = mViewDataBinding.amount.text.toString()

            if (amount.isEmpty()) {
                mViewDataBinding.root.snackbar("Enter Amount")
                return@setOnClickListener
            }
            params.addProperty("amount", amount.toInt())

            mViewModel.withdrawal(params)
        }

        if (!mViewModel.withdrawalResponse.hasActiveObservers()) {
            mViewModel.withdrawalResponse.observe(requireActivity()) {
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
                            popUpStack()

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


}