package com.visualpro.realproject.repositories.network.local_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.visualpro.myapplication.Model.Definition
import com.visualpro.myapplication.Model.Word
import com.visualpro.realproject.model.WordTypeSeparate
import com.visualpro.realproject.model.model_relations.Word_DefinitionList_Ref

@Dao
interface category_DAO {
    @Transaction
    @Query("SELECT * FROM word")
    fun viewWordList():List<Word_DefinitionList_Ref>

     @Insert
     fun insertWord(word:Word)

     @Insert
     fun insertDefinition(definition: Definition?)

     @Insert
     fun insertWordTypeSeparate(separate: WordTypeSeparate)

     @Query("SELECT * FROM word WHERE word=:word")
     fun selectWord(word:String): Word_DefinitionList_Ref?

//    @Query("SELECT word FROM word")
//    fun selectWordNma():List<String>

//     @Query("SELECT * FROM word WHERE ")
//     fun getSeparateByWordPrimaryKey(key:String)
////    @Query ("INSERT INTO category VALUES (:item)")
//    fun updateCategoryItem(position : Int, item:Category)



}