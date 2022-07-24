package com.example.wcclone.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.wcclone.Fragments.ChatsFragment
import com.example.wcclone.Fragments.StatusFragment
import com.example.wcclone.Fragments.CallFragment

class FragementAdapater(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ChatsFragment()
            1 -> StatusFragment()
            2 -> CallFragment()
            else -> ChatsFragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        if (position == 0) {
            title = "CHATS"
        }
        if (position == 1) {
            title = "STATUS"
        }
        if (position == 2) {
            title = "CALLS"
        }
        return title
    }
}