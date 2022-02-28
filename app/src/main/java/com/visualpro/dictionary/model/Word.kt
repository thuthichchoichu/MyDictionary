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
open class Word(
    var word: String="",
    var wordType: String="",
    var pronUs: String="",
    var pronUk: String="",
    var auUrlUs: String="",
    var auUrlUk: String="",
    var nearByWord: ArrayList<NearByWord> = ArrayList()
) : Parcelable, BaseObservable() {
    @PrimaryKey
    @ColumnInfo(name = "word_ID")
    var pKey=getPK()
    @ColumnInfo(name = "categoryName")
    var belongToCategory: String=""
//    }
    fun getPK()= word+wordType
    var userSave=false
    }








