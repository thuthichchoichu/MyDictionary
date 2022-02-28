package com.visualpro.realproject.model.model_relations


import androidx.room.Embedded
import androidx.room.Relation
import com.visualpro.myapplication.Model.Definition
import com.visualpro.myapplication.Model.Word
import com.visualpro.realproject.model.WordTypeSeparate
data class UserWord_DefinitionList_Ref(
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
) {

    fun getWordDetails(): String {
        var example= "";
        var j = 0;
        for (i in 0 until (defList.size)) {
            val tempList = defList.get(i)?.example
            if (tempList != null) {
                example = if (tempList.size > 0) example else ""
                if (!example.equals("")) {
                    j = i
                    break
                }
            }
        }

        if(defList[j]!!.isNull){
            j++
        }
        val textDisplay = word.word+ " " + " : " + defList.get(j)!!.definition


        return if (example.equals("")) textDisplay else textDisplay + "\n" + "   - " + example;
    }

}
