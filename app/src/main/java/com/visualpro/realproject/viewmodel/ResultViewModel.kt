package com.visualpro.realproject.viewmodel

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.visualpro.myapplication.Model.Definition
import com.visualpro.myapplication.Model.Word
import com.visualpro.realproject.Model.TranslateItems
import com.visualpro.realproject.Model.WordTypeSeparate
import com.visualpro.realproject.Model.model_relations.Word_DefinitionList_Ref
import com.visualpro.realproject.network.DataInterface_Result
import com.visualpro.realproject.repositories.Repository

class ResultViewModel(val mRepo: Repository, path: String) : ViewModel(), DataInterface_Result,
    Observable {
    init {
        mRepo.resultInterface = this
        mRepo.localFilePath = path
    }
    var listFaild = ArrayList<String>()

    private val callbacks = PropertyChangeRegistry()

    @Bindable
    private var mDefinition = MutableLiveData<Word_DefinitionList_Ref>()

    @Bindable
    private var mDefinitionOf = MutableLiveData("")

    @Bindable
    private var mTranslateItem = MutableLiveData<TranslateItems>()

    @Bindable
    private var mSoundsReady = MutableLiveData<BooleanArray>()

    @Bindable
    private var mSoundPoolHolder = MutableLiveData<IntArray>()// // 0 : us sound 1 :sound uk

    @Bindable
    var mListCategoryName = MutableLiveData<List<String>>()

    @Bindable
    var mWordTypeSeparate = MutableLiveData<List<WordTypeSeparate>>()

    override fun nextWord(word: String, sucess: Boolean) {
        if (!sucess) {
            listFaild.add(word)
        }
    }

    fun performLoadNearByWord(word: String, url: String) {
//        mRepo!!.performParseHtml(word, url, true, false, processed, size)
    }


    fun playSoundUs() {
        mRepo.playSoundUs(mDefinition.value?.word!!.word)
    }

    fun playSoundUk() {
        mRepo.playSoundUk(mDefinition.value?.word!!.word)
    }


    fun getDefinitionOf(): String {
        return "definition of " + mDefinitionOf.value!! + " in English"
    }

    fun getWord(): Word? {
        getWordType()
        return mDefinition.value?.word
    }

    fun getWordRef(): Word_DefinitionList_Ref? {

        return mDefinition.value
    }

    fun getWordType(): String {
        var wordTpe = "["
        if(mWordTypeSeparate.value!=null)
        for (i in mWordTypeSeparate.value!!.indices) {
                wordTpe +=  mWordTypeSeparate.value!!.get(i).wordType
                if(mWordTypeSeparate.value!!.size-i>1){
                    wordTpe +=", "

            }

        }
        return wordTpe+"]"
    }


    fun getSeperate(): List<WordTypeSeparate>? {
        return mWordTypeSeparate.value
    }

    fun getTranslateItem(): TranslateItems? {
        return mTranslateItem.value
    }


    override fun setWordItem(item: Word_DefinitionList_Ref) {
        mDefinition.value = item
        mDefinitionOf.value = item.word.word
        mWordTypeSeparate.value = item.separate
        notifyChange()
    }

    override fun setSoundReady(soundPool: Int, tag: Int) {
        if (mSoundPoolHolder.value == null) {
            mSoundPoolHolder.value = IntArray(2)
        }
        Log.d("test", "tag${tag}, sound${soundPool}")
        mSoundPoolHolder.value?.set(tag, soundPool)

    }

    override fun setListCategorysName(list: List<String>) {
        mListCategoryName.postValue(list)
    }

    fun saveWordToDb(position: Int) {
        var list :ArrayList<Definition> = mDefinition.value?.defList as ArrayList<Definition>
        for (i in list.indices){
            list.get(i).belongtoCategory=position+1
        }
        var word=getWord()!!
        word.user_Save=true
        mRepo.addWordToCategory(getWord()!!,list, position) }

    fun getWordFromServer(
        word: String?,
        url: String?,
        useUrl: Boolean,
        processed: Int,
        size: Int
    ) {
        if (word == null || url == null) {
            return
        }
        mSoundPoolHolder.value?.set(0, -1)
        mSoundPoolHolder.value?.set(1, -1)
        mRepo!!.performParseHtml1(word, url, useUrl)
    }

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

    class ResultViewModelFactory(private val respository: Repository, var path: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ResultViewModel(respository, path) as T
            }

            throw IllegalArgumentException("Unknown ViewModel class")

        }
    }
}