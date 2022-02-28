package com.visualpro.dictionary.model.User

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.visualpro.myapplication.Model.Definition
import com.visualpro.myapplication.Model.Word
import kotlinx.parcelize.Parcelize

@Parcelize
class WordWithDefinitions(
    @Embedded var word: Word,
    @Relation(parentColumn = "word_ID",
    entityColumn = "word_ID", entity = Definition::class)
    var userDefinition: List<Definition>
) : Parcelable {
    fun getWordDetails(): String {
        var example= "";
        var j = 0;
        var stop=false
        for (i in 0 until (userDefinition.size)) {
            if(!stop){
                val tempList = userDefinition.get(i).example
                for(i in tempList.indices){
                    if(tempList[i].isUserExample){
                        example=tempList[i].example
                        j=i
                        stop=true
                        break
                    }
                }
//
//
//                example = if (tempList.size > 0) example else ""
//                if (!example.equals("")) {
//                    j = i
//                    break
//                }
            }else{
                break
            }

        }

        if(userDefinition[j].isNull){
            j++
        }
        val textDisplay = word.word+ " " + " : " + userDefinition.get(j)!!.definition


        return if (example.equals("")) textDisplay else textDisplay + "\n\n" + "   + " + example;
    }
}


