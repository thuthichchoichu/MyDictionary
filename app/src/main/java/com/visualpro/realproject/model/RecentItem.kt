package com.visualpro.realproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent")
data class RecentItem(var word: String, var isDefinition: Boolean = true) {
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
}