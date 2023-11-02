package com.teamx.hatly.ui.fragments.parcel.activeParcel

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.hatly.BR
import com.teamx.hatly.R
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.data.remote.Resource
import com.teamx.hatly.databinding.FragmentActiveBinding
import com.teamx.hatly.ui.fragments.orders.active.ActiveAdapter
import com.teamx.hatly.ui.fragments.orders.active.ActiveViewModel
import com.teamx.hatly.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActiveParcelFragment : BaseFragment<FragmentActiveBinding, ActiveViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_active
    override val viewModel: Class<ActiveViewModel>
        get() = ActiveViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    //    lateinit var productAdapter: ActiveAdapter
    lateinit var activeOrderAdapter: ActiveAdapter

    //    lateinit var productArrayList: ArrayList<String>
    lateinit var activeOrderArrayList: ArrayList<com.teamx.hatly.data.dataclasses.getActiveorder.Doc>

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



        mViewModel.getActiveOrder("accepted", "parcel")

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
        }

        ActiveOrderRecyclerview()


//        productRecyclerview()
//
//        productArrayList.add("")
//        productArrayList.add("")
//        productArrayList.add("")
//        productArrayList.add("")
//
//        productAdapter.notifyDataSetChanged()

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