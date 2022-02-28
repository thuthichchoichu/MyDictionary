package com.visualpro.dictionary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.visualpro.dictionary.model.User.WordWithDefinitions
import com.visualpro.dictionary.repositories.Repository

class DetailViewModel(val mRepo: Repository) : ViewModel(){

    var mWordDetailsData = MutableLiveData<java.util.ArrayList<WordWithDefinitions>>()
    var lastEdit = ""

    var categoryName: String = ""
    var categoryColor:Int = -1
    fun updateCategoryWithNewColor( color:Int){
        categoryColor=color
        mRepo.updateCatoryColor(categoryName, color)
    }


//    fun getWordsByCategory(categoryName: String) {
//        mRepo.getWordsByCategory(categoryName){
//            mWordDetailsData.value=it
//        }
//    }
    class DetailActivityViewModelFactory(
        private val respository: Repository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return DetailViewModel(respository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")

        }
    }

    fun deleteThisCategory() {

        mRepo.deleteCategoryWithName(categoryName)
    }

    fun changeCategoryName(oldCategoryNameTemp: String, newCategoryName:String) {
        mRepo.changeCategoryname(oldCategoryNameTemp, newCategoryName)
    }


}