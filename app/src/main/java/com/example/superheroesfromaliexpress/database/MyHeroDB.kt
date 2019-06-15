package com.example.superheroesfromaliexpress.database

import android.content.Context
import androidx.room.Room

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */

object MyHeroDB {
    private var database: MyHeroDatabase? = null

    fun getInstance(context: Context):MyHeroDatabase?{

        if (database == null){
            database = Room.databaseBuilder(context, MyHeroDatabase::class.java, "my-hero-db")
                .fallbackToDestructiveMigration()
                .build()
        }


        return database

    }
}