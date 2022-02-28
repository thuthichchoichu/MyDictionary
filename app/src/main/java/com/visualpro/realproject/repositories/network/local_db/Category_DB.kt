package com.visualpro.realproject.repositories.network.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.visualpro.myapplication.Model.Category
import com.visualpro.myapplication.Model.Definition
import com.visualpro.myapplication.Model.Word
import com.visualpro.realproject.model.DailyWord
import com.visualpro.realproject.model.RecentItem
import com.visualpro.realproject.model.WordTypeSeparate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Category::class, Word::class, Definition::class, WordTypeSeparate::class, RecentItem::class, DailyWord::class],
    version = 2
)
@TypeConverters(WordConverter::class)
abstract class Category_DB : RoomDatabase() {


    abstract fun CategoryDAO(): category_DAO
    abstract fun userDAO(): User_DAO

    companion object {
        const val DATABASE_NAME = "category.db"
        private var INSTANCE: Category_DB? = null

        fun getInstance(context: Context, scope: CoroutineScope): Category_DB {
            if (INSTANCE != null) {
                return INSTANCE as Category_DB
            }
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Category_DB::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(CategoryDatabaseCallBack(scope))
                    .build()
                INSTANCE = instance
                instance
            }

        }

        private class CategoryDatabaseCallBack(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        database.CategoryDAO()
                    }

                }
            }
        }
    }


}