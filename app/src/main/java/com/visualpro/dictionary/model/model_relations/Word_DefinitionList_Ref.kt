package com.visualpro.dictionary.model.model_relations

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.visualpro.myapplication.Model.Definition
import com.visualpro.myapplication.Model.Word
import com.visualpro.dictionary.model.WordTypeSeparate
import kotlinx.parcelize.Parcelize

@Parcelize
data class Word_DefinitionList_Ref(
    @Embedded var word: Word, @Relation(
        parentColumn = "word_ID", entityColumn = "word_ID"
    ) var defList: List<Definition?>,

    @Relation(
        parentColumn = "word_ID", entityColumn = "word_ID"
    ) var separate: List<WordTypeSeparate>
) : Parcelable {

}
