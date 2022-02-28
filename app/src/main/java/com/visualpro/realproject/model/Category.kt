package com.visualpro.myapplication.Model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "category")
@Parcelize
data class Category(
    @PrimaryKey(autoGenerate = false)
    var categoryName: String = "",
    var lastEdit: String = "",
    var color: Int = -1,
    var isPinned: Boolean = false
) : Parcelable {
    companion object {
        const val SMALLEST: String = "small"
        const val MEDIUM: String = "medium"
        const val BIGGEST: String = "biggest"

    }





}






