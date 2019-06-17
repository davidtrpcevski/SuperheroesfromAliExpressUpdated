package com.example.superheroesfromaliexpress.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */

abstract class SuperHeroAbstractActivity : AppCompatActivity() {

    abstract fun disposeResources()

    abstract fun setView() : Int

    abstract fun initView()

    abstract fun initLateInitVars()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setView())
        initView()
        initLateInitVars()
    }

    override fun onDestroy() {
        disposeResources()
        super.onDestroy()
    }
}