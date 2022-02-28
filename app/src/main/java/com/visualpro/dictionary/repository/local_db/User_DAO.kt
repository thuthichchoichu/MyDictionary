package com.visualpro.dictionary.repository.network.local_db

import androidx.room.*
import com.visualpro.dictionary.model.DailyWord
import com.visualpro.dictionary.model.RecentItem
import com.visualpro.dictionary.model.WordTypeSeparate
import com.visualpro.dictionary.model.model_relations.Category_WordList_Ref
import com.visualpro.dictionary.model.model_relations.UserWord_DefinitionList_Ref
import com.visualpro.dictionary.model.model_relations.Word_DefinitionList_Ref
import com.visualpro.myapplication.Model.Category
import com.visualpro.myapplication.Model.Definition
import com.visualpro.myapplication.Model.Word
import kotlinx.coroutines.flow.Flow


@Dao
interface User_DAO {
    @Transaction
    @Query("SELECT * FROM  category")
    fun getAllCategory(): Flow<List<Category_WordList_Ref>>

    @Insert
    fun insertCategory(item: Category)

    @Insert
    fun insertWord(word: Word)

    @Update
    fun UserSaveWord(word: Word)

    @Insert
    fun insertDefinition(definition: List<Definition>?)

    @Insert
    fun insertWordTypeSeparate(separate: WordTypeSeparate)

    @Insert
    fun insertRecentWord(word: RecentItem)

    @Query("SELECT * FROM recent ORDER BY id DESC LIMIT 5 ")
    fun get6ItemRecent(): Flow<List<RecentItem>>

    @Query("SELECT * FROM word WHERE word = (SELECT word FROM recent ORDER BY id DESC LIMIT 1 )")
    fun getItemRecent(): Word_DefinitionList_Ref?


    @Query("SELECT categoryName FROM category")
    fun getListCategorysName():List<String>

    @Query("SELECT categoryName FROM category")
    fun getListCategorysName1(): List<String>

    @Transaction
    @Query("SELECT * FROM word WHERE word in (SELECT word from word WHERE categoryName=:categoryName)")
    fun selectWordsByCategory(categoryName: String): List<UserWord_DefinitionList_Ref>

    @Query("DELETE FROM category WHERE categoryName=:categoryName")
    fun delecategory(categoryName: String)

    @Query("UPDATE word SET categoryName='' WHERE word in(SELECT word FROM word WHERE categoryName=:categoryName)")
    fun deleteWordFromCategory(categoryName: String)

    @Insert
    fun insertDailyWord(dailyWord: DailyWord)

    @Query("SELECT * FROM `daily word` ORDER BY id DESC LIMIT 1")
    fun getDailyWord(): Flow<DailyWord>

    @Transaction
    @Query("SELECT * FROM word")
    fun viewWordList(): List<Word_DefinitionList_Ref>


    @Insert
    fun insertDefinition(definition: Definition?)

    @Update
    fun upDateDefinition(definition: Definition?)


    @Query("SELECT * FROM word WHERE word=:word")
    fun selectWord(word: String): Word_DefinitionList_Ref?

    @Query("UPDATE category SET categoryName=:newCategoryName WHERE categoryName=:oldCategoryNameTemp")
    fun renameCategory(oldCategoryNameTemp: String, newCategoryName: String) {
    }


    @Query("UPDATE word SET categoryName=:newCategoryName WHERE categoryName=:oldCategoryName")
    fun changeWordOwner(newCategoryName: String, oldCategoryName: String)

    @Query("SELECT * FROM CATEGORY where categoryName=:newCategoryName")
    fun getCategoryWithName(newCategoryName: String): Category


    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCategory(category: Category)

    @Query("UPDATE category SET color=:color WHERE categoryName=:categoryName")
    fun updateCategoryColor(categoryName: String, color: Int)

    @Query("SELECT auUrlUk FROM word WHERE word =:wordName")
    fun getAudioLinkByWordName(wordName: String?):String

    @Query("UPDATE CATEGORY SET lastEdit =:lastEdit WHERE categoryName=:categoryName ")
    fun setCurrentTime(categoryName: String, lastEdit:String) {

    }


}
