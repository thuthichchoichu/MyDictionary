package com.visualpro.realproject.viewmodel



import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.visualpro.realproject.model.model_relations.Category_WordList_Ref
import com.visualpro.realproject.repositories.Repository

class FavoriteViewModel1(private val mRepo: Repository): ViewModel(){
    var     categoryData: LiveData<List<Category_WordList_Ref>> = mRepo.categoryData.asLiveData()

    fun getCategoryData():List<Category_WordList_Ref>?{
        return categoryData.value
    }
    class FavoriteFragmentViewModelFactory(private val respository:Repository) :ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(FavoriteViewModel1::class.java)){
                @Suppress("UNCHECKED_CAST")
                return FavoriteViewModel1(respository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")

        }
    }


    fun getWordsByCategory(categoryID:Int){
//        mRepo.getWordsByCategory(categoryID)
    }

    fun saveCurrentWordToDB() {

    }


}





