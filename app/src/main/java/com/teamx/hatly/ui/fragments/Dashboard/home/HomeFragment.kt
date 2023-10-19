package com.teamx.hatly.ui.fragments.Dashboard.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.hatly.BR
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.databinding.FragmentHomeBinding
import com.teamx.hatly.ui.fragments.orders.Incoming.IncomingAdapter
import com.teamx.hatly.utils.SlideToUnlock
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(), SlideToUnlock.OnSlideToUnlockEventListener {

    override val layoutId: Int
        get() = com.teamx.hatly.R.layout.fragment_home
    override val viewModel: Class<HomeViewModel>
        get() = HomeViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private lateinit var options: NavOptions
    lateinit var productAdapter: IncomingAdapter

    lateinit var productArrayList: ArrayList<String>
    lateinit var productArrayList1: ArrayList<String>

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.lifecycleOwner = viewLifecycleOwner

        options = navOptions {
            anim {
                enter = com.teamx.hatly.R.anim.enter_from_left
                exit = com.teamx.hatly.R.anim.exit_to_left
                popEnter = com.teamx.hatly.R.anim.nav_default_pop_enter_anim
                popExit = com.teamx.hatly.R.anim.nav_default_pop_exit_anim
            }
        }
       mViewDataBinding.slideToUnlock.externalListener = this
       productRecyclerview()
       productRecyclerview1()

       productArrayList.add("")
       productArrayList.add("")
       productArrayList.add("")
       productArrayList.add("")

       productAdapter.notifyDataSetChanged()


       productArrayList1.add("")
       productArrayList1.add("")
       productArrayList1.add("")
       productArrayList1.add("")

       productAdapter.notifyDataSetChanged()




   }

    private fun productRecyclerview() {
        productArrayList = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        mViewDataBinding.recyclerViewPastOrder.layoutManager = linearLayoutManager

        productAdapter = IncomingAdapter(productArrayList)
        mViewDataBinding.recyclerViewPastOrder.adapter = productAdapter

    }
    private fun productRecyclerview1() {
        productArrayList1 = ArrayList()

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        mViewDataBinding.recyclerView2.layoutManager = linearLayoutManager

        productAdapter = IncomingAdapter(productArrayList1)
        mViewDataBinding.recyclerView2.adapter = productAdapter

    }

    override fun onSlideToUnlockCanceled() {
    }

    override fun onSlideToUnlockDone() {
        Log.d("slider", "onSlideToUnlockDone: ")

    }



}