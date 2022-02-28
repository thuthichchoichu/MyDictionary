package com.visualpro.realproject.viewmodel

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.visualpro.myapplication.Model.Category
import com.visualpro.myapplication.Model.Word
import com.visualpro.realproject.model.RecentItem
import com.visualpro.realproject.model.WordTypeSeparate
import com.visualpro.realproject.model.model_relations.Word_DefinitionList_Ref
import com.visualpro.realproject.repositories.Repository
import com.visualpro.realproject.repositories.network.submodel.SearchText

class DefinitionViewModel(val mRepo: Repository, path: String) : ViewModel(), Observable {
    init {
        mRepo.localFilePath = path
    }
    private val callbacks = PropertyChangeRegistry()


    @Bindable
    private var mDefinition = MutableLiveData<Word_DefinitionList_Ref>().apply {
        mRepo.retrieveLastestQueryWord{
            value=it
            mWordTypeSeparate.value = it?.separate
            notifyChange()
        }
    }

//    @Bindable
//    var mListCategoryName = mRepo.listCategoryName.asLiveData()

    @Bindable
    var mWordTypeSeparate = MutableLiveData<List<WordTypeSeparate>>()


    fun getWord(): Word? {
        getWordType()
        return mDefinition.value?.word
    }

    fun getWordRef(): Word_DefinitionList_Ref? {
        return mDefinition.value
    }

    fun getWordType(): String {
        var wordTpe = "["
        if (mWordTypeSeparate.value != null)
            for (i in mWordTypeSeparate.value!!.indices) {
                wordTpe += mWordTypeSeparate.value!!.get(i).wordType
                if (mWordTypeSeparate.value!!.size - i > 1) {
                    wordTpe += ", "

                }

            }
        return wordTpe + "]"
    }


    fun getSeperate(): List<WordTypeSeparate>? {
        return mWordTypeSeparate.value
    }



//    override fun setListCategorysName(list: List<String>) {
//        mListCategoryName.value=list
//    }

//    fun saveWordToDb(categoryName:String) {
////        var list: ArrayList<Definition> = mDefinition.value?.defList as ArrayList<Definition>
////        for (i in list.indices) {
////            list.get(i).belongtoCategory = position + 1
////        }
////        var word = getWord()!!
////        word.belongToCategory=categoryName
////        mRepo.addWordToCategory(word)
////    }
//
//
//    fun getWordFromServer(word: String?, url: String?, useUrl: Boolean, processed: Int, size: Int) {
//        if (word == null || url == null) {
//            return
//        }
//        mRepo!!.loadWord(word, url, useUrl, true)
//    }
//
//    fun playSoundUs() {
//
//        mRepo.playSoundUs(mDefinition.value?.word!!.auUrlUs)
//    }
//
//    fun playSoundUk() {
//        mRepo.playSoundUk(mDefinition.value?.word!!.auUrlUk)
//    }

//    fun setSoundReady(soundPool: Int, tag: Int) {
//        if (mSoundPoolHolder.value == null) {
//            mSoundPoolHolder.value = IntArray(2)
//        }
////        Log.d("test", "tag${tag}, sound${soundPool}")
//        mSoundPoolHolder.value?.set(tag, soundPool)
//
//    }

    fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }

    fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.remove(callback)
    }

    private var searchItem = MutableLiveData<ArrayList<SearchText>>()
    var searchItem_Response: LiveData<ArrayList<SearchText>> = searchItem

    fun getWordFromServer(word: String?, url: String, useUrl: Boolean) {
        addWordToRecent(RecentItem(word!!, true))
        mRepo.loadWord(word, url, useUrl,true)
    }

    private fun addWordToRecent(word: RecentItem) {
        mRepo.addWordToRecent(word)
    }

    fun querySeach(searchQuery: String) {
        mRepo.search(searchQuery){
            searchItem.value=it
        }
    }

    fun addCategory( item: Category){
        mRepo.addEmptyCategory(item)
    }

    class DefinitionViewModelFactory(private val respository: Repository, var x: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DefinitionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DefinitionViewModel(respository, x) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")

        }

    }
}