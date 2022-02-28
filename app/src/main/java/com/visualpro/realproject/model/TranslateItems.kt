package com.visualpro.realproject.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TranslateItems(
    var textIn1: String = "",
    var textOut1: String = ""
) : Parcelable {

    //    var sentences : Array<SubTranslateResponse>
    var inLanguage_Index: Int = 0
    var outLanguage_Index: Int = 0
//    fun getTextIn():String{
//        return sentences[0].textIn
//    }
//    fun getTextOut():String{
//        return sentences[0].textOut
//    }

}
