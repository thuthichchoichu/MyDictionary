package com.visualpro.realproject.Model

import android.os.Parcelable
import com.visualpro.realproject.network.submodel.SubTranslateResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class TranslateItems( var sentences : Array<SubTranslateResponse>) : Parcelable {
    var inLanguage_Index: Int = 0
    var outLanguage_Index: Int = 0
    fun getTextIn():String{
        return sentences[0].textIn
    }
    fun getTextOut():String{
        return sentences[0].textOut
    }

}
