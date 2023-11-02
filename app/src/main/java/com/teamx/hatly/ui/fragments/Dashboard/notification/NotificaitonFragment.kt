package com.teamx.hatly.ui.fragments.Dashboard.notification

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamx.hatly.BR
import com.teamx.hatly.R
import com.teamx.hatly.baseclasses.BaseFragment
import com.teamx.hatly.data.remote.Resource
import com.teamx.hatly.databinding.FragmentNotificationBinding
import com.teamx.hatly.ui.fragments.Dashboard.notification.modelNotification.Doc
import com.teamx.hatly.utils.TimeFormatter
import com.teamx.hatly.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificaitonFragment : BaseFragment<FragmentNotificationBinding, NotificationViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_notification
    override val viewModel: Class<NotificationViewModel>
        get() = NotificationViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel
    var layoutManager2: LinearLayoutManager? = null

    var isScrolling = false
    var currentItems = 0
    var totalItems = 0
    var scrollOutItems = 0

    private lateinit var notificationArrayList: ArrayList<Doc>
    private lateinit var hatlyPopularAdapter: NotificationAdapter

    @RequiresApi(Build.VERSION_CODES.O)
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


        notificationArrayList = ArrayList()
        layoutManager2 = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        mViewDataBinding.recNotification.layoutManager = layoutManager2
        hatlyPopularAdapter = NotificationAdapter(notificationArrayList)
        mViewDataBinding.recNotification.adapter = hatlyPopularAdapter


        mViewModel.notification()

        if (!mViewModel.notificationResponse.hasActiveObservers()) {
            mViewModel.notificationResponse.observe(requireActivity()) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Resource.Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.let { data ->
                            notificationArrayList.clear()
                            data.docs.forEach {
                                it.createdAt = TimeFormatter.formatTimeDifference(it.createdAt)
                                notificationArrayList.add(it)
                            }
//                            notificationArrayList.addAll(data.docs)
                            hatlyPopularAdapter.notifyDataSetChanged()

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

}