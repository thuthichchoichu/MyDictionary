package com.visualpro.realproject.views

import android.app.Application
import com.visualpro.realproject.network.local_db.Category_DB
import com.visualpro.realproject.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy {Category_DB.getInstance(this, applicationScope)}
    val repository by lazy { Repository(database.CategoryDAO()) }
}