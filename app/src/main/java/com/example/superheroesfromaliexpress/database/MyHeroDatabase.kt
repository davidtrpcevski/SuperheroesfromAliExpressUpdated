package com.example.superheroesfromaliexpress.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */

@Database(entities = [MyHeroModel::class], version = 1, exportSchema = false)
abstract class MyHeroDatabase : RoomDatabase() {
    abstract fun dao() : MyHeroDAO
}