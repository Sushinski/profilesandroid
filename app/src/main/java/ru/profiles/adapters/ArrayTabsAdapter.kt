package ru.profiles.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ArrayTabsAdapter(val mFragmentManager: FragmentManager, val mTabs: Array<Fragment>)
    : FragmentStatePagerAdapter(mFragmentManager) {


    override fun getItem(position: Int): Fragment {
        return mTabs[position]
    }

    override fun getCount(): Int {
        return mTabs.count()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }
}