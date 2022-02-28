package com.visualpro.dictionary

import android.app.Application
import com.visualpro.dictionary.repositories.Repository
import com.visualpro.dictionary.repositories.network.local_db.Category_DB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication : Application() {
    companion object{
        const val LANGUAGE_TRANSLATE_SETTING="language setting"
    }
    val sharePreference by lazy {getSharedPreferences(LANGUAGE_TRANSLATE_SETTING, MODE_PRIVATE) }
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy {Category_DB.getInstance(this, applicationScope)}
    val repository by lazy { Repository(database.userDAO()) }
}