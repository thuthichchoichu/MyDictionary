package com.visualpro.myapplication.Model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
@Parcelize
@Entity(tableName = "definition" ,indices = [androidx.room.Index(value = ["word_ID"], unique = false)])
data class Definition(  var definition: String="", var userExample: ArrayList<String> = ArrayList(), val oxfordExample: ArrayList<String> = ArrayList()) : Parcelable{
    var expand:Boolean=false
    var isNull:Boolean=false
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
    @ColumnInfo(name="word_ID")
    var belongToWord:String=""
    fun reverseExpand(){
        expand=!expand
    }
}


