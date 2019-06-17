package com.example.superheroesfromaliexpress.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crazylegend.kotlinextensions.livedata.context
import com.crazylegend.kotlinextensions.retrofit.RetrofitResult
import com.example.superheroesfromaliexpress.api.Api
import com.example.superheroesfromaliexpress.davidretrofit.DavidRetrofit
import com.example.superheroesfromaliexpress.model.SuperHeroModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */

class DetailedSuperHeroVM(application: Application, private val id: Int) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val retrofit by lazy {
        DavidRetrofit.createRetrofit()?.create(Api::class.java)
    }



    private val dataModel: MutableLiveData<RetrofitResult<SuperHeroModel>> = MutableLiveData()
    val data: LiveData<RetrofitResult<SuperHeroModel>> = dataModel

    init {
        retrofit?.let { api ->
            dataModel.value = RetrofitResult.Loading
            api.getByID(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->

                    if (response.isSuccessful) {

                        response.body()?.apply {
                            dataModel.value = RetrofitResult.Success(this)
                        }

                    } else {
                        dataModel.value = RetrofitResult.ApiError(context, response.code(), response.errorBody() )
                    }

                }, {
                    dataModel.value = RetrofitResult.Error(it.message.toString(), Exception(it), it)
                }).addTo(compositeDisposable)
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
        super.onCleared()
    }

}