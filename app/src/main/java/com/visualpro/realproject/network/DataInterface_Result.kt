package com.visualpro.realproject.network

import com.visualpro.realproject.Model.model_relations.Word_DefinitionList_Ref

interface DataInterface_Result {
     fun setWordItem(item: Word_DefinitionList_Ref)
     fun setSoundReady(soundPool: Int, tag:Int)
     fun setListCategorysName(list:List<String>)
     fun nextWord(word:String, sucess:Boolean)
}