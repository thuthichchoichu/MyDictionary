package com.visualpro.myapplication.Model

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "word",indices = [Index(value = ["word"], unique = true)])
data class Word(
    val word: String,
    val wordType: String,
    val pronUs: String,
    val pronUk: String,
    val auUrlUs: String,
    val auUrlUk: String,
    val nearByWord: ArrayList<NearByWord>
) : Parcelable, BaseObservable() {
    @PrimaryKey
    @ColumnInfo(name = "word_ID")
    var pKey=getPK()

    var Category_Id: Int = -1


//    var nearByWords:Int=0
//
//    var separateList=ArrayList<WordTypeSeparate>()
//    fun addSeparate(separate:WordTypeSeparate){
//        separateList.add(separate)
//    }
    fun getPK():String{
        return word+wordType
    }





}

