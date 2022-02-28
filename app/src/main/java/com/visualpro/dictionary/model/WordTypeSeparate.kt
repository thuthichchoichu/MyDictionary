package com.visualpro.dictionary.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "separate",indices = [androidx.room.Index(value = ["word_ID"], unique = false)])
class WordTypeSeparate : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var x:Int=0
    @ColumnInfo(name = "word_ID")
    var wordRef:String=""

    var wordType:String=""
    var begin:Int=0
    var end:Int=-1
}