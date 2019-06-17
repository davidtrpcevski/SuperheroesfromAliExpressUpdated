package com.example.superheroesfromaliexpress.core

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */

class CustomViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList : ArrayList<Fragment> = ArrayList()
    private val fragmentListNames : ArrayList<String> = ArrayList()


    fun addFragment(fragment: Fragment, title:String){
        fragmentList.add(fragment)
        fragmentListNames.add(title)
    }

    override fun getPageTitle(position: Int) = fragmentListNames[position]

    override fun getItem(position: Int) = fragmentList[position]

    override fun getCount() = fragmentList.size
}