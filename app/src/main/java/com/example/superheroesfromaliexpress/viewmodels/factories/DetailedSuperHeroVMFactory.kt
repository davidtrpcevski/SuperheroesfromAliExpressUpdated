package com.example.superheroesfromaliexpress.viewmodels.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.superheroesfromaliexpress.viewmodels.DetailedSuperHeroVM

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */

class DetailedSuperHeroVMFactory(private val application: Application, private val id:Int) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailedSuperHeroVM(application, id) as T
    }

}