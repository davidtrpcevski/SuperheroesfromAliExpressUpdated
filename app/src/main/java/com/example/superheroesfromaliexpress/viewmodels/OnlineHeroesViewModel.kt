package com.example.superheroesfromaliexpress.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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

class OnlineHeroesViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val retrofit by lazy {
        DavidRetrofit.createRetrofit()?.create(Api::class.java)
    }


    private val listData: MutableLiveData<RetrofitResult<List<SuperHeroModel>>> = MutableLiveData()
    val list: LiveData<RetrofitResult<List<SuperHeroModel>>> = listData


    init {
        callApi()
    }

    private fun callApi() {
        retrofit?.let { api ->
            listData.value = RetrofitResult.Loading
            api.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->

                    if (response.isSuccessful) {

                        response.body()?.apply {
                            listData.value = RetrofitResult.Success(this)
                        }

                    } else {
                        //handle api error with error code
                        listData.value = RetrofitResult.ApiError(context, response.code(), response.errorBody() )
                    }

                }, {
                    listData.value = RetrofitResult.Error(it.message.toString(), Exception(it), it)
                }).addTo(compositeDisposable)
        }
    }


    private val query: MutableLiveData<String> = MutableLiveData()

    fun setSearchQuery(text:String){
        query.postValue(text)
    }

    fun filterData(): LiveData<List<SuperHeroModel>> {
        return Transformations.switchMap(query){
            applyFilterData(it)
        }
    }

    private var filteredList: MutableLiveData<List<SuperHeroModel>> = MutableLiveData()

    private fun applyFilterData(serch: String?): LiveData<List<SuperHeroModel>> {

        val filteredApplyList: ArrayList<SuperHeroModel> = ArrayList()
        listData.value?.let {
            if (it is RetrofitResult.Success) {
                it.value.forEach {
                    it.name.apply {
                        if (this.toLowerCase().contains(serch.toString().toLowerCase())) {
                            filteredApplyList.add(it)
                        }
                    }
                }
            }
        }


        filteredList.value = filteredApplyList

        return filteredList

    }

    override fun onCleared() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun refreshData() {
        callApi()
    }
}