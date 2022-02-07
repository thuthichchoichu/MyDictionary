package com.visualpro.realproject.viewmodel

import android.content.Context
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.*
import com.visualpro.myapplication.Model.Category
import com.visualpro.myapplication.Model.SearchItem_Local
import com.visualpro.realproject.Model.TranslateItems
import com.visualpro.realproject.Model.model_relations.Category_WordList_Ref
import com.visualpro.realproject.Model.model_relations.UserWord_DefinitionList_Ref
import com.visualpro.realproject.network.DataInterface_Main
import com.visualpro.realproject.network.submodel.SearchItemResponse
import com.visualpro.realproject.repositories.Repository

class MainActivityViewModel(private val mRepo: Repository): ViewModel(), DataInterface_Main, Observable {
    init {
        mRepo.mainInterface=this
    }
    private val callbacks = PropertyChangeRegistry()
    companion object {
        const val DATA_DEFINITION = "load definition"
    }

    var mWordDetailsData=MutableLiveData<List<UserWord_DefinitionList_Ref>>()
    var mCategoryWordListLiveData: LiveData<List<Category_WordList_Ref>> = mRepo.allCategory.asLiveData()

    private var mSearchItem = MutableLiveData<ArrayList<SearchItem_Local>>()
    private var mTranslateItem = MutableLiveData<TranslateItems>()
    get(){return field}





    fun getCategoryData():List<Category_WordList_Ref>?{
            return mCategoryWordListLiveData.value
    }
    fun mSearchItem_Instance(): MutableLiveData<ArrayList<SearchItem_Local>> {
        return mSearchItem
    }

    fun mTranslateItem_Instance(): MutableLiveData<TranslateItems> {
        return mTranslateItem
    }

    fun queryTranslate(textTranslate: String) {
        mRepo.performTranslate(textTranslate)
    }

    fun querySeach(searchQuery: String) {
        mRepo.performSearches(searchQuery)
    }


    override fun setSearchItems(list: ArrayList<SearchItem_Local>) {
        mSearchItem.postValue(list)
    }

    override suspend fun setSearchItems(item: SearchItemResponse?) {
        if (item != null) {
            val arr = item.resposeList
            val list = ArrayList<SearchItem_Local>()
            for (i in arr.indices) {
                list.add(SearchItem_Local(arr.get(i).searchText!!, false))
            }
            mSearchItem.postValue(list)

        }

    }

    override suspend fun setTranslateItem(item: TranslateItems) {
        mTranslateItem.postValue(item)
    }

      fun addCategory(context: Context, item:Category){
        mRepo.addEmptyCategory(item)
    }
    class MainActivityViewModelFactory(private val respository:Repository) :ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(respository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")

        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
       callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.remove(callback)
    }
    fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }
    fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }


    fun getWordsByCategory(categoryID:Int){
        mRepo.getWordsByCategory(categoryID)
    }

    override fun setWordDetails(list: List<UserWord_DefinitionList_Ref>) {
        mWordDetailsData.postValue(list)
    }



}





