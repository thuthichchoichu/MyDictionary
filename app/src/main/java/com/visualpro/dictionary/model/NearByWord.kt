package com.visualpro.myapplication.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NearByWord(val word: String, val urlToThisWord: String, val wordType: String) :
    Parcelable
