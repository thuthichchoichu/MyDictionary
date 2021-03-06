package com.visualpro.dictionary.viewmodel

import androidx.lifecycle.*
import com.visualpro.dictionary._interface.CallbackSoundLoaded
import com.visualpro.dictionary.model.RecentItem
import com.visualpro.dictionary.model.WordTypeSeparate
import com.visualpro.dictionary.model.model_relations.Word_DefinitionList_Ref
import com.visualpro.dictionary.repository.Repository
import com.visualpro.dictionary.repository.network.submodel.SearchText
import com.visualpro.myapplication.Model.Category
import com.visualpro.myapplication.Model.Definition
import com.visualpro.myapplication.Model.Word
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel( val mRepo: Repository, path: String) : ViewModel(),
    CallbackSoundLoaded {
    init {
        mRepo.localFilePath = path
        mRepo.soundCallBack = this
        viewModelScope.launch {
            delay(2000)
            _stateSplashScreen.value=false
        }
    }
    var mWordTypeSeparate = MutableLiveData<List<WordTypeSeparate>>()

    fun getListCategoryName(callback:(List<String>) ->Unit){
        mRepo.getListCategoryName {
            callback(it)
        }
    }

    private var _stateSplashScreen= MutableStateFlow(true)
    var stateSplashScreen=_stateSplashScreen.asStateFlow()
    fun getWord(): Word? {
        return mDefinition.value?.word
    }

    private var _currentDisplayWordIsFavorite = MutableLiveData(false)
    var currentDisplayWordIsFavorite: LiveData<Boolean> = _currentDisplayWordIsFavorite
    fun saveWordToDb(categoryName: String) {
        getWord()!!.apply {
            _currentDisplayWordIsFavorite.value = true
            userSave = true
            belongToCategory = categoryName
            mRepo.addWordToCategory(this)
        }
    }


    private var searchItem = MutableLiveData<ArrayList<SearchText>>()
    var searchItem_Response: LiveData<ArrayList<SearchText>> = searchItem
    fun querySeach(searchQuery: String) {
        mRepo.search(searchQuery) {
            searchItem.value = it
        }
    }

    var isSoundUsLoaded = false
    var isSoundUkLoaded = false
    fun playSoundUs() = mRepo.playSoundUs(mDefinition.value?.word?.auUrlUs)
    fun playSoundUk() = mRepo.playSoundUk(mDefinition.value?.word?.auUrlUk)
    fun playDailyWordSound(word: String?)=mRepo.playSoundByWordName(word)
    var mDefinition = MutableLiveData<Word_DefinitionList_Ref>().apply {
        mRepo.retrieveLastestQueryWord {
            value = it
            mWordTypeSeparate.value = it?.separate

        }
    }

    fun getWordFromServer(word: String, url: String, useUrl: Boolean) {
        addWordToRecent(RecentItem(word!!, true))
        mRepo.loadWord(word, url, useUrl, true) {
            _currentDisplayWordIsFavorite.value = it?.word!!.userSave
            mDefinition.value = it
            mWordTypeSeparate.value = it.separate
        }
    }

    private fun addWordToRecent(word: RecentItem) {
        mRepo.addWordToRecent(word)
    }

    fun addCategory(item: Category) {
        mRepo.addEmptyCategory(item)
    }

    private var _soundUsLoadState = MutableLiveData(false)
    private var _soundUkLoadState = MutableLiveData(false)
    var soundUsLoadState: LiveData<Boolean> = _soundUsLoadState
    var soundUkLoadState: LiveData<Boolean> = _soundUkLoadState
    override fun onSoundLoadComplete(soundType: String) {
        if (soundType.equals("US")) {
            _soundUsLoadState.value = true
        } else {
            _soundUkLoadState.value = true
        }
    }

    override fun onSoundLoadFail(soundType: String) {
        if (soundType.equals("US")) {
            _soundUsLoadState.value = false
        } else {
            _soundUkLoadState.value = false
        }
    }

    fun setUserDefinition(definition: Definition) {
        mRepo.addNewUserDefinition(definition)
    }

    var beginAnition = MutableLiveData(false)

    class MainActivityViewModelFactory(
        private val respository: Repository,
        private val path: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return MainActivityViewModel(respository, path) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")

        }
    }
}





