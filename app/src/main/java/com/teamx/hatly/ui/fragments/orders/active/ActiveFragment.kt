package com.teamx.hatly.ui.fragments.orders.active

import android.os.Bundle
import android.view.View
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.hatly.BR
import com.teamx.hatly.R
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.data.dataclasses.pastorder.Doc
import com.teamx.hatly.data.remote.Resource
import com.teamx.hatly.databinding.FragmentActiveParcelBinding
import com.teamx.hatly.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActiveFragment : BaseFragment<FragmentActiveParcelBinding, ActiveViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_active_parcel
    override val viewModel: Class<ActiveViewModel>
        get() = ActiveViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    lateinit var activeOrderAdapter: ActiveAdapter

    lateinit var activeOrderArrayList: ArrayList<Doc>

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

        try {
            mViewModel.getPastOrders(1,10,"accepted")
        } catch (e: Exception) {

        }

        if (!mViewModel.getPastOrdersResponse.hasActiveObservers()) {
            mViewModel.getPastOrdersResponse.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            data.docs.forEach {
                                activeOrderArrayList.add(it)
                            }

                            activeOrderAdapter.notifyDataSetChanged()


                        }
                    }

                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(
                            requireContext(),
                            it.message!!
                        )
                    }
                }
                if (isAdded) {
                    mViewModel.getPastOrdersResponse.removeObservers(
                        viewLifecycleOwner
                    )
                }
            }
        }



/*
        mViewModel.getActiveOrder("accepted", "order")

        if (!mViewModel.getActiveOrderResponse.hasActiveObservers()) {
            mViewModel.getActiveOrderResponse.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            data.docs.forEach {
                                activeOrderArrayList.add(it)
                            }

                            activeOrderAdapter.notifyDataSetChanged()


                        }
                    }

                    Resource.Status.ERROR -> {
                        loadingDialog.dismiss()
                        DialogHelperClass.errorDialog(
                            requireContext(),
                            it.message!!
                        )
                    }
                }
                if (isAdded) {
                    mViewModel.getActiveOrderResponse.removeObservers(
                        viewLifecycleOwner
                    )
                }
            }
        }*/







        ActiveOrderRecyclerview()


    }

    private fun ActiveOrderRecyclerview() {
        activeOrderArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.activeRecyclerView.layoutManager = linearLayoutManager

        activeOrderAdapter = ActiveAdapter(activeOrderArrayList)
        mViewDataBinding.activeRecyclerView.adapter = activeOrderAdapter

    }


//    private fun productRecyclerview() {
//        productArrayList = ArrayList()
//
//        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//        mViewDataBinding.activeRecyclerView.layoutManager = linearLayoutManager
//
//        productAdapter = ActiveAdapter(productArrayList)
//        mViewDataBinding.activeRecyclerView.adapter = productAdapter
//
//    }


}