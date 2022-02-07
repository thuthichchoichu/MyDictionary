package com.visualpro.realproject.network

import com.visualpro.myapplication.Model.SearchItem_Local
import com.visualpro.realproject.Model.TranslateItems
import com.visualpro.realproject.Model.model_relations.UserWord_DefinitionList_Ref
import com.visualpro.realproject.network.submodel.SearchItemResponse

interface DataInterface_Main {
    fun setWordDetails(list:List<UserWord_DefinitionList_Ref>)
    fun setSearchItems(list : ArrayList<SearchItem_Local>)
    suspend fun setSearchItems(list : SearchItemResponse?)
    suspend fun setTranslateItem(item : TranslateItems)
}