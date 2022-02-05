package com.visualpro.myapplication.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class SearchItem_Local(val word: String, var isHistory: Boolean) : Parcelable{
    override fun equals(other: Any?): Boolean {
        if(other is SearchItem_Local){
            return other.word.equals(this.word)
        }
        return false
    }

    override fun hashCode(): Int {
        return Objects.hashCode(word)
    }
}
