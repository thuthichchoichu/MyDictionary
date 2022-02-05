package com.visualpro.realproject.network.local_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.visualpro.myapplication.Model.Category
import com.visualpro.myapplication.Model.Definition
import com.visualpro.myapplication.Model.Word
import com.visualpro.realproject.Model.WordTypeSeparate
import com.visualpro.realproject.Model.model_relations.Category_WordList_Ref
import com.visualpro.realproject.Model.model_relations.Word_DefinitionList_Ref
import kotlinx.coroutines.flow.Flow

@Dao
interface category_DAO {
    @Insert
    fun insertCategory(item:Category)
    @Transaction
    @Query("SELECT * FROM category")
    fun getAllCategory():Flow<List<Category_WordList_Ref>>

    @Transaction
    @Query("SELECT * FROM category WHERE Category_Id = :position")
    fun getCategoryAt(position: Int): Category

    @Query ("UPDATE category SET categoryName = :newCategoryName WHERE Category_Id = :position")
    fun updateCategoryName(position : Int, newCategoryName:String)

    @Transaction
    @Query("SELECT categoryName FROM category")
    fun getListCategorysName():List<String>


//    @Query("SELECT wordList FROM category WHERE id=:position")
//    fun getWordsList(position:Int):Flow<List<Word>>
//
    @Transaction
    @Query("SELECT * FROM category")
    fun viewCategoryList():List<Category_WordList_Ref>

    @Transaction
    @Query("SELECT * FROM word")
    fun viewWordList():List<Word_DefinitionList_Ref>

     @Insert
     fun insertWord(word:Word)
//
    @Insert
    fun insertUserWord(word:Word)

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