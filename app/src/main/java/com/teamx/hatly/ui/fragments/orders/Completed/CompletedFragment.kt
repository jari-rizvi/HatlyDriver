package com.teamx.hatly.ui.fragments.orders.Completed

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.hatly.BR
import com.teamx.hatly.R
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.data.dataclasses.getorders.PastDispatche
import com.teamx.hatly.data.remote.Resource
import com.teamx.hatly.databinding.FragmentCompletedBinding
import com.teamx.hatly.utils.DialogHelperClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompletedFragment : BaseFragment<FragmentCompletedBinding, CompletedViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_completed
    override val viewModel: Class<CompletedViewModel>
        get() = CompletedViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private lateinit var options: NavOptions


    lateinit var pastOrderAdapter: PastOrderAdapter
    lateinit var pastOrderArrayList: ArrayList<PastDispatche>

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


        mViewModel.getOPastrders("delivered")

        if (!mViewModel.getOPastrdersResponse.hasActiveObservers()) {
            mViewModel.getOPastrdersResponse.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            data.pastDispatches.forEach {
                                pastOrderArrayList.add(it)
                            }

                            pastOrderAdapter.notifyDataSetChanged()


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
                    mViewModel.getOPastrdersResponse.removeObservers(
                        viewLifecycleOwner
                    )
                }
            }
        }

        OrderRecyclerview()
    }

    private fun OrderRecyclerview() {
        pastOrderArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        mViewDataBinding.pastRecyclerView.layoutManager = linearLayoutManager

        pastOrderAdapter = PastOrderAdapter(pastOrderArrayList)
        mViewDataBinding.pastRecyclerView.adapter = pastOrderAdapter

    }

}