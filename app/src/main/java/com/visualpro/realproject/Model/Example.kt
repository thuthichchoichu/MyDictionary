package com.visualpro.realproject.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Example(var example: String, var isUserExample:Boolean=false) : Parcelable {
}