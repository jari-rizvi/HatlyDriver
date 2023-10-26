package com.teamx.hatly.ui.fragments.orders.Incoming

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.teamx.hatly.BR
import com.teamx.hatly.R
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.databinding.FragmentIncomingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncomingFragment : BaseFragment<FragmentIncomingBinding, IncomingViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_incoming
    override val viewModel: Class<IncomingViewModel>
        get() = IncomingViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private lateinit var options: NavOptions

//    lateinit var productAdapter: IncomingAdapter

//    lateinit var productArrayList: ArrayList<String>

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

//        productArrayList = ArrayList()
////        productRecyclerview()
//
//        productArrayList.add("")
//        productArrayList.add("")
//        productArrayList.add("")
//        productArrayList.add("")

//        productAdapter.notifyDataSetChanged()




    }

//    private fun productRecyclerview() {
//        productArrayList = ArrayList()
//
//        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//        mViewDataBinding.activeRecyclerView.layoutManager = linearLayoutManager
//
//        productAdapter = IncomingAdapter(productArrayList)
//        mViewDataBinding.activeRecyclerView.adapter = productAdapter
//
//    }


}