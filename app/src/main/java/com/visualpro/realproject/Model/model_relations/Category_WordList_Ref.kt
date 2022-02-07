package com.visualpro.realproject.Model.model_relations

import androidx.room.Embedded
import androidx.room.Relation
import com.visualpro.myapplication.Model.Category
import com.visualpro.myapplication.Model.Word
import com.visualpro.realproject.Model.User.WordWithDefinitions


data class Category_WordList_Ref(
    @Embedded var category: Category,
    @Relation(
        parentColumn = "Category_Id",
        entityColumn = "Category_Id", entity = Word::class
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