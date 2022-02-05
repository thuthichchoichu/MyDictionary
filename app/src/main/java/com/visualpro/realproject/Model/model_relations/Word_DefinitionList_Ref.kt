package com.visualpro.realproject.Model.model_relations

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.visualpro.myapplication.Model.Definition
import com.visualpro.myapplication.Model.Word
import com.visualpro.realproject.Model.WordTypeSeparate
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Word_DefinitionList_Ref(
    @Embedded var word: Word,
    @Relation(
        parentColumn = "word_ID",
        entityColumn = "word_ID"
    )
    var defList:List<Definition?>,

    @Relation(
        parentColumn = "word_ID",
        entityColumn = "word_ID"
    )
    var separate:List<WordTypeSeparate>
) : Parcelable {
    fun getWordDetails(): String {
        var example= "";
        var j = 0;
        for (i in 0 until (defList.size)) {
            val tempList = defList.get(i)?.userExample
            if (tempList != null) {
                example = if (tempList.size > 0) example else ""
                if (!example.equals("")) {
                    j = i
                    break
                }
            }
        }


        val textDisplay = word.word + " " + " : " + defList.get(j)!!.definition


        return if (example.equals("")) textDisplay else textDisplay + "\n" + "   - " + example;
    }
}
