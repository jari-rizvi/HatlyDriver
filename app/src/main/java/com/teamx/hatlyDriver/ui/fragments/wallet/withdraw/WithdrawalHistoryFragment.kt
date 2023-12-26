package com.teamx.hatlyDriver.ui.fragments.wallet.withdraw

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.hatlyDriver.BR
import com.teamx.hatlyDriver.MainApplication
import com.teamx.hatlyDriver.R
import com.teamx.hatlyDriver.baseclasses.BaseFragment
import com.teamx.hatlyDriver.data.dataclasses.withdrawalHistory.Doc
import com.teamx.hatlyDriver.data.remote.Resource
import com.teamx.hatlyDriver.databinding.FragmentWithdrawalHistoryBinding
import com.teamx.hatlyDriver.localization.LocaleManager
import com.teamx.hatlyDriver.ui.fragments.wallet.WalletViewModel
import com.teamx.hatlyDriver.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WithdrawalHistoryFragment : BaseFragment<FragmentWithdrawalHistoryBinding, WalletViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_withdrawal_history
    override val viewModel: Class<WalletViewModel>
        get() = WalletViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    var isScrolling = false
    var hasNextPage = false
    var nextPage = 1
    var currentItems = 0
    var totalItems = 0
    var scrollOutItems = 0

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

        mViewModel.withdrawalHisotory(10, 1)
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
                            if (!hasNextPage) {
                                withdrawalHistoryArrayList.clear()
                            }

                            data.docs.forEach {
                                withdrawalHistoryArrayList.add(it)
                            }
                            nextPage = data.nextPage  ?: 1
                            hasNextPage = data.hasNextPage
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



        withdrawalHistoryArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.withdrawalRec.layoutManager = linearLayoutManager

        withdrawalHistoryAdapter = WithdrawalAdapter(withdrawalHistoryArrayList)
        mViewDataBinding.withdrawalRec.adapter = withdrawalHistoryAdapter

        mViewDataBinding.withdrawalRec.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                currentItems = linearLayoutManager.childCount
                totalItems = linearLayoutManager.itemCount
                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition()

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    if (hasNextPage) {
                        mViewModel.withdrawalHisotory(10, nextPage)
                    }
                }
            }
        })



    }

}