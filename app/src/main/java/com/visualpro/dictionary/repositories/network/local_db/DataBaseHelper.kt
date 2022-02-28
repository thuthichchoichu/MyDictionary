package com.visualpro.dictionary.repositories.network.local_db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, Category_DB.DATABASE_NAME, null, 2) {
    override fun onCreate(db: SQLiteDatabase?) {}
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
}