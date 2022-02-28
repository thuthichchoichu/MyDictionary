package com.visualpro.realproject.repositories.network.submodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//data class TranslateResponse(val sentences :ArrayList<SubTranslateResponse>,val dict:ArrayList<Dict>,var src:String, val confidence :Double, val spell : Spell, val ld_results : LdResults )
@Parcelize
data class SubTranslateResponse(@SerializedName("trans") val textOut :String,@SerializedName("orig") val textIn :String, @SerializedName("backend") val backEnd : Int) :
    Parcelable {

}
//data class Dict (val pos:String,val terms : Array<String>, val entry:ArrayList<SubEntry>, val base_form:String, val pos_enum:Int)
//data class SubEntry(val word : String, val reverse_translation : Array<String>, val score:Double)
//data class LdResults(val srclangs : Array<String>, val srclangs_confidences :Array<Double>, val extended_srclangs :Array<String>  )
//class Spell()

