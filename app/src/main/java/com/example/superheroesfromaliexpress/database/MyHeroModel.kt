package com.example.superheroesfromaliexpress.database

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.BLOB
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by David Trpchevski (trpcevski.david@gmail.com) on 15 June 2019
 */

@Entity(tableName = "myHeroes")
data class MyHeroModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "gender")
    var gender : String = "",

    @ColumnInfo(name = "race")
    var race : String = "",

    @ColumnInfo(name = "image", typeAffinity = BLOB)
    var image : ByteArray = ByteArray(0)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MyHeroModel

        if (id != other.id) return false
        if (name != other.name) return false
        if (gender != other.gender) return false
        if (race != other.race) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + race.hashCode()
        result = 31 * result + image.contentHashCode()
        return result
    }
}