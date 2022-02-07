package com.visualpro.realproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.visualpro.realproject.Model.model_relations.UserWord_DefinitionList_Ref
import com.visualpro.realproject.network.DataInterface_Details
import com.visualpro.realproject.repositories.Repository

class DetailViewModel( val mRepo:Repository) : ViewModel(), DataInterface_Details {
    init {
        mRepo.detailInterface=this
    }
     var mWordDetailsData = MutableLiveData<List<UserWord_DefinitionList_Ref>>()
     var categoryNameLiveData = MutableLiveData<String>()


    fun getWordsByCategory(categoryID:Int){
        mRepo.getWordsByCategory(categoryID)
    }
//
//    override fun setWordDetails(list: List<UserWord_DefinitionList_Ref>) {
//        mWordDetailsData.postValue(list)
//    }

    fun getCategoryNam(): String {
        return categoryNameLiveData.value ?: ""
    }
    fun getCategoryList() :List<UserWord_DefinitionList_Ref>?{
        return mWordDetailsData.value
    }

    class DetailActivityViewModelFactory(private val respository: Repository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(DetailViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(respository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")

        }
    }

    override fun setWordDetails(list: List<UserWord_DefinitionList_Ref>) {
        mWordDetailsData.postValue(list)
    }


}