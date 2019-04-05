package ru.profiles.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.ArrayList

class FragmentTabsAdapter(val mFragmentManager: FragmentManager, val mTabs: Map<String, Fragment>)
    : FragmentStatePagerAdapter(mFragmentManager) {

    private val mFragmentList = ArrayList<Fragment>(mTabs.size)
    private val mNames = ArrayList<String>(mTabs.size)

    init{
        for(f in mTabs.iterator()){
            mFragmentList.add(f.value)
            mNames.add(f.key)
        }
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.count()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mNames[position]
    }
}