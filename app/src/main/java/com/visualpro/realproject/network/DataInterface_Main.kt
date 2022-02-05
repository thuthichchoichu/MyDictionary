package com.visualpro.realproject.network

import com.visualpro.myapplication.Model.SearchItem_Local
import com.visualpro.realproject.Model.TranslateItems
import com.visualpro.realproject.network.submodel.SearchItemResponse

interface DataInterface_Main {
    fun setSearchItems(list : ArrayList<SearchItem_Local>)
    suspend fun setSearchItems(list : SearchItemResponse?)
    suspend fun setTranslateItem(item : TranslateItems)
}