package com.teamx.hatly.ui.fragments.Dashboard.Profile

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.teamx.hatly.R
import com.teamx.hatly.BR
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_profile
    override val viewModel: Class<ProfileViewModel>
        get() = ProfileViewModel::class.java
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



    }

}