package com.teamx.hatly.ui.fragments.orders

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.teamx.hatly.R
import com.teamx.hatly.BR
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.databinding.FragmentNewPassBinding
import com.teamx.hatly.databinding.FragmentOrdersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : BaseFragment<FragmentOrdersBinding, OrdersViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_orders
    override val viewModel: Class<OrdersViewModel>
        get() = OrdersViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private lateinit var options: NavOptions

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

        mViewDataBinding.imgBack.setOnClickListener {
            popUpStack()
        }

        setupViewPager()
        setupTabLayout()


    }

    private fun setupTabLayout() {
        TabLayoutMediator(
            mViewDataBinding.tabLayout, mViewDataBinding.viewPager
        ) { tab, position ->

            when (position) {
                0 -> {
                    tab.text = "Active"
                }
                1 -> {
                    tab.text = "Incoming"
                }
                2 -> {
                    tab.text = "Special"
                }
                3 -> {
                    tab.text = "Completed"
                }
            }

            Log.d("setupTabLayout", "setupTabLayout: $position")

//            {tab.text = "Tab " + (position + 1)}

        }.attach()
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(requireActivity(), 4)
        mViewDataBinding.viewPager.adapter = adapter
    }


}