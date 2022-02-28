package com.visualpro.dictionary

import android.app.Application
import android.util.Log
import com.visualpro.dictionary.repository.Repository
import com.visualpro.dictionary.repository.network.local_db.Category_DB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication : Application() {
    companion object{
        const val LANGUAGE_TRANSLATE_SETTING="language setting"

        fun Float.log(msg:String)= Log.d("test", msg+"->"+this)
        fun Int.log(msg:String)= Log.d("test", msg+"->"+this)
        fun String.log(msg:String)= Log.d("test", msg+"->"+this)
        fun Double.log(msg:String)= Log.d("test", msg+"->"+this)
        fun Long.log(msg:String)= Log.d("test", msg+"->"+this)
        fun Boolean.log(msg:String)= Log.d("test", msg+"->"+this)
    }
    val sharePreference by lazy {getSharedPreferences(LANGUAGE_TRANSLATE_SETTING, MODE_PRIVATE) }
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy {Category_DB.getInstance(this, applicationScope)}
    val repository by lazy { Repository(database.userDAO()) }





}