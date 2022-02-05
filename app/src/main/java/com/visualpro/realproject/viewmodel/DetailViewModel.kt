package com.visualpro.realproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.visualpro.realproject.Model.model_relations.Word_DefinitionList_Ref
import com.visualpro.realproject.repositories.Repository

class DetailViewModel( val mRepo:Repository) : ViewModel() {
     var categoryMutableLiveData = MutableLiveData<ArrayList<Word_DefinitionList_Ref>>()
     var categoryNameLiveData = MutableLiveData<String>()

    fun getCategoryNam(): String {
        return categoryNameLiveData.value ?: ""
    }
    fun getCategoryList() :ArrayList<Word_DefinitionList_Ref>{
        return categoryMutableLiveData.value?: ArrayList()
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


}