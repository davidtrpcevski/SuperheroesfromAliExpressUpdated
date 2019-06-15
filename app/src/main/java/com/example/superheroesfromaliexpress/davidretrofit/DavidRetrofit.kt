package com.example.superheroesfromaliexpress.davidretrofit

import com.example.superheroesfromaliexpress.core.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */
object DavidRetrofit {

    private var retrofit: Retrofit? = null

    fun createRetrofit(): Retrofit? {

        val okHttpClient = OkHttpClient.Builder()

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpClient.addInterceptor(loggingInterceptor)

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClient.build())
                .build()
        }

        return retrofit
    }
}