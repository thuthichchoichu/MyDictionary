package com.visualpro.myapplication.Model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.visualpro.realproject.Model.Example
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "definition",
    indices = [androidx.room.Index(value = ["word_ID"], unique = false)]
)
open class Definition(
    var definition: String = "",
    var example: ArrayList<Example> = ArrayList()
) : Parcelable {
    var expand: Boolean = false
    var isNull: Boolean = false

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "word_ID")
    var belongToWord: String = ""

    @ColumnInfo(name = "Category_ID")
    var belongtoCategory: Int = 0
    fun reverseExpand() {
        expand = !expand
    }
}


