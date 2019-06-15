package com.example.superheroesfromaliexpress.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.superheroesfromaliexpress.database.MyHeroDB
import com.example.superheroesfromaliexpress.database.MyHeroModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */

class MyHeroDatabaseVM(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val database by lazy {
        MyHeroDB.getInstance(application)
    }

    private val myHeroes: MutableLiveData<List<MyHeroModel>> = MutableLiveData()
    val heroes: LiveData<List<MyHeroModel>> = myHeroes

    init {
        getAll()
    }

    fun getAll() {
        database?.apply {
            dao().getAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        myHeroes.value = it
                    }, {
                        it.printStackTrace()
                    }).addTo(compositeDisposable)
        }
    }

    fun insertModel(myHeroModel: MyHeroModel, calllback: () -> Unit) {
        database?.apply {
            dao().insertHero(myHeroModel)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        calllback()
                    }, {
                        it.printStackTrace()
                    }).addTo(compositeDisposable)
        }
    }

    fun updateModel(myHeroModel: MyHeroModel) {
        database?.apply {
            dao().updateHero(myHeroModel)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe().addTo(compositeDisposable)
        }
    }

    fun deleteModel(myHeroModel: MyHeroModel) {
        database?.apply {
            dao().deleteHero(myHeroModel)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe().addTo(compositeDisposable)
        }
    }


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}