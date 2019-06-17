package com.example.superheroesfromaliexpress.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */

@Dao
interface MyHeroDAO {

    @Query("select * from myHeroes")
    fun getAll(): Flowable<List<MyHeroModel>>

    @Insert(onConflict = REPLACE)
    fun insertHero(hero: MyHeroModel): Completable

    @Update(onConflict = REPLACE)
    fun updateHero(hero: MyHeroModel): Completable

    @Delete
    fun deleteHero(hero: MyHeroModel): Completable


}