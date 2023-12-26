package com.teamx.hatlyDriver.ui.fragments.wallet

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.hatlyDriver.BR
import com.teamx.hatlyDriver.MainApplication
import com.teamx.hatlyDriver.R
import com.teamx.hatlyDriver.baseclasses.BaseFragment
import com.teamx.hatlyDriver.data.dataclasses.withdrawalHistory.Doc
import com.teamx.hatlyDriver.data.remote.Resource
import com.teamx.hatlyDriver.databinding.FragmentWalletBinding
import com.teamx.hatlyDriver.localization.LocaleManager
import com.teamx.hatlyDriver.ui.fragments.wallet.withdraw.WithdrawalAdapter
import com.teamx.hatlyDriver.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WalletFragment : BaseFragment<FragmentWalletBinding, WalletViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_wallet
    override val viewModel: Class<WalletViewModel>
        get() = WalletViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    lateinit var transactionHistoryAdapter: TransactionAdapter
    lateinit var transactionHistoryArrayList: ArrayList<com.teamx.hatlyDriver.data.dataclasses.transactionHistory.Doc>

  lateinit var withdrawalHistoryAdapter: WithdrawalAdapter
    lateinit var withdrawalHistoryArrayList: ArrayList<Doc>

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

        mViewDataBinding.btnAddBank.setOnClickListener {

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
            findNavController().popBackStack()
        }

        mViewDataBinding.imgTopUp.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.topUpFragment, arguments, options)
        }

        mViewDataBinding.imgwithdraw.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.withDrawFragment, arguments, options)
        }
        mViewDataBinding.btnAddBank.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.bankDetailsFragment, arguments, options)
        }


        mViewDataBinding.textView39.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.transcationHistoryFragment, arguments, options)
        }
        mViewDataBinding.textView391.setOnClickListener {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.withdrawalHistoryFragment, arguments, options)
        }

        TransactionRecyclerview()
        WithdrawalRecyclerview()
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
                            mViewDataBinding.textView37.text = try {
                                data.wallet.toString()
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


        mViewModel.trancationHisotory(5, 1)
        if (!mViewModel.transactionHistoryResponse.hasActiveObservers()) {
            mViewModel.transactionHistoryResponse.observe(requireActivity()) {
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
                            data.docs.forEach {
                                transactionHistoryArrayList.add(it)
                            }

                            transactionHistoryAdapter.notifyDataSetChanged()
                        }
                    }

                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        mViewDataBinding.root.snackbar(it.message!!)
                    }
                }
            }
        }




        mViewModel.withdrawalHisotory(5, 1)
        if (!mViewModel.withdrawalHistoryResponse.hasActiveObservers()) {
            mViewModel.withdrawalHistoryResponse.observe(requireActivity()) {
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
                            data.docs.forEach {
                                withdrawalHistoryArrayList.add(it)
                            }

                            withdrawalHistoryAdapter.notifyDataSetChanged()
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


    private fun TransactionRecyclerview() {
        transactionHistoryArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.Tranrecycler.layoutManager = linearLayoutManager

        transactionHistoryAdapter = TransactionAdapter(transactionHistoryArrayList)
        mViewDataBinding.Tranrecycler.adapter = transactionHistoryAdapter

    }
    private fun WithdrawalRecyclerview() {
        withdrawalHistoryArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.WithDrawrecycler.layoutManager = linearLayoutManager

        withdrawalHistoryAdapter = WithdrawalAdapter(withdrawalHistoryArrayList)
        mViewDataBinding.WithDrawrecycler.adapter = withdrawalHistoryAdapter

    }


}