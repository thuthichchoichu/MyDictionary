package com.visualpro.dictionary.model.model_relations

import androidx.room.Embedded
import androidx.room.Relation
import com.visualpro.myapplication.Model.Category
import com.visualpro.myapplication.Model.Word
import com.visualpro.dictionary.model.User.WordWithDefinitions


data class Category_WordList_Ref(
    @Embedded var category: Category,
    @Relation(
        parentColumn = "categoryName",
        entityColumn = "categoryName", entity = Word::class
    )
    var wordList: List<WordWithDefinitions>,
) {

    fun getSize(): String {
        when (wordList.size) {
            0, 1 -> return Category.SMALLEST
            2, 3 -> return Category.MEDIUM;
            else -> {
                return Category.BIGGEST
            }
        }
    }
}