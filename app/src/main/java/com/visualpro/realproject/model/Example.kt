package com.visualpro.realproject.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Example(var example: String, var isUserExample:Boolean=false) : Parcelable {
    var moreDescription:String?=null
}