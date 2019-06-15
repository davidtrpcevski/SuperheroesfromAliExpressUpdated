package com.example.superheroesfromaliexpress.ui.activities

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.superheroesfromaliexpress.R
import com.example.superheroesfromaliexpress.core.CustomViewPagerAdapter
import com.example.superheroesfromaliexpress.core.SuperHeroAbstractActivity
import com.example.superheroesfromaliexpress.core.VIEW_PAGER_POS
import com.example.superheroesfromaliexpress.ui.fragments.OfflineHeroesFragment
import com.example.superheroesfromaliexpress.ui.fragments.OnlineHeroesFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */
class MainActivity : SuperHeroAbstractActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var customViewPagerAdapter: CustomViewPagerAdapter

    private val offlineHeroesFragment by lazy {
        OfflineHeroesFragment()
    }

    private val onlineHeroesFragment by lazy {
        OnlineHeroesFragment()
    }

    override fun disposeResources() {

    }

    override fun setView(): Int = R.layout.activity_main

    override fun initView() {
        toolbar = a_toolbar
        viewPager = a_viewpager
        tabLayout = a_tablayout

        setSupportActionBar(toolbar)
    }

    override fun initLateInitVars() {
        customViewPagerAdapter = CustomViewPagerAdapter(supportFragmentManager)
        customViewPagerAdapter.addFragment(onlineHeroesFragment, getString(R.string.onh))
        customViewPagerAdapter.addFragment(offlineHeroesFragment, getString(R.string.ofh))
        viewPager.adapter = customViewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }


    override fun onSaveInstanceState(outState: Bundle) {

        outState.putInt(VIEW_PAGER_POS, viewPager.currentItem)

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewPager.currentItem = savedInstanceState.getInt(VIEW_PAGER_POS)
    }

}
