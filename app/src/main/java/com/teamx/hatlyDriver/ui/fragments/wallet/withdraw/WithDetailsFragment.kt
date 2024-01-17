package com.teamx.hatlyDriver.ui.fragments.wallet.withdraw

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.navigation.navOptions
import com.squareup.picasso.Picasso
import com.teamx.hatlyDriver.BR
import com.teamx.hatlyDriver.MainApplication
import com.teamx.hatlyDriver.R
import com.teamx.hatlyDriver.baseclasses.BaseFragment
import com.teamx.hatlyDriver.data.remote.Resource
import com.teamx.hatlyDriver.databinding.FragmentWithdrawalDetailsBinding
import com.teamx.hatlyDriver.localization.LocaleManager
import com.teamx.hatlyDriver.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WithDetailsFragment : BaseFragment<FragmentWithdrawalDetailsBinding, WithdrawalViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_withdrawal_details
    override val viewModel: Class<WithdrawalViewModel>
        get() = WithdrawalViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var id: String

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


        val bundle = arguments
        id = bundle?.getString("id").toString()
        mViewModel.withdrawalDetails(id)


        mViewModel.withdrawalDetailsResponse.observe(requireActivity(), Observer {
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

                        if (data.status == "accepted") {
                            mViewDataBinding.textView48.visibility = View.VISIBLE
                            mViewDataBinding.textView488.visibility = View.GONE
                            mViewDataBinding.textView409.visibility = View.GONE
                        }
                        if (data.status == "rejected") {
                            mViewDataBinding.textView48.visibility = View.GONE
                            mViewDataBinding.textView488.visibility = View.VISIBLE
                            mViewDataBinding.textView409.visibility = View.GONE
                        }
                        if (data.status == "pending") {
                            mViewDataBinding.textView409.visibility = View.VISIBLE
                            mViewDataBinding.textView488.visibility = View.GONE
                            mViewDataBinding.textView48.visibility = View.GONE
                        }


                        Picasso.get().load(data.screenShort).into(mViewDataBinding.imageView23)

                    }
                }

                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogHelperClass.errorDialog(requireContext(), it.message!!)
                }
            }
            if (isAdded) {
                mViewModel.withdrawalDetailsResponse.removeObservers(
                    viewLifecycleOwner
                )
            }
        })


    }


}