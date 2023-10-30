package com.teamx.hatly.ui.fragments.orders

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teamx.hatly.ui.fragments.orders.Completed.CompletedFragment
import com.teamx.hatly.ui.fragments.orders.Incoming.IncomingFragment
import com.teamx.hatly.ui.fragments.orders.active.ActiveFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity, private var totalCount: Int) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return totalCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ActiveFragment()
            1 -> IncomingFragment()
            2 -> CompletedFragment()
            else -> ActiveFragment()
        }
    }

}