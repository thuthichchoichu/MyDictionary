package com.visualpro.dictionary.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily word")

data class DailyWord(
    val dayOfWord: String,
    val word: String,
    val definition: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
}
