package com.teamx.hatly.ui.fragments.orders.special

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.teamx.hatly.R
import com.teamx.hatly.BR
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.databinding.FragmentOrdersBinding
import com.teamx.hatly.databinding.FragmentSpecialBinding
import com.teamx.hatly.ui.fragments.orders.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpecialFragment : BaseFragment<FragmentSpecialBinding, SpecialViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_special
    override val viewModel: Class<SpecialViewModel>
        get() = SpecialViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

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




    }




}