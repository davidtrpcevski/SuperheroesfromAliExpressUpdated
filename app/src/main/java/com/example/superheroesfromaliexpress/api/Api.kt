package com.example.superheroesfromaliexpress.api

import com.example.superheroesfromaliexpress.core.BASE_ROUTE
import com.example.superheroesfromaliexpress.model.SuperHeroModel
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */

interface Api {


    @GET(BASE_ROUTE + "all.json")
    fun getAll(): Flowable<Response<List<SuperHeroModel>>>

    @GET(BASE_ROUTE + "id/{id}.json")
    fun getByID(@Path("id") id: Int): Single<Response<SuperHeroModel>>

}