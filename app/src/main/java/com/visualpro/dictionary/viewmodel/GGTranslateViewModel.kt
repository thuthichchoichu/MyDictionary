package com.visualpro.dictionary.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.ui.platform.AndroidUiDispatcher.Companion.Main
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.visualpro.dictionary.TapToTranslateService
import com.visualpro.dictionary.model.TranslateItems
import com.visualpro.dictionary.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(InternalCoroutinesApi::class)
class GGTranslateViewModel(
    private var mRepo: Repository,
    sharePreference: SharedPreferences,
    private val listCountry: Array<String>,
    private val listCountryEntry: Array<String>
) : ViewModel(){
    val mCoroutine= CoroutineScope(IO)
    init {
        mRepo.sharedPreferences = sharePreference
    }

    private var _indexSrcItemLanguageSelected = MutableLiveData<Int>().apply {
        value = mRepo.getLanguageInputIndex()
    }
    private var _indexDesItemLanguageSelected = MutableLiveData<Int>().apply {
        value = mRepo.getLanguageOutputIndex()
    }
//    private var _intputLanguageSeleteted = MutableLiveData<String>().apply {
//        if (detectLanguageEnable.value == true) {
//            value = "Auto detect"
//        } else {
//            value = mRepo.getLangIn()
//        }
//
//    }
//    private var _outputLanguageSeleteted = MutableLiveData<String>().apply {
//        value = mRepo.getLangOut()
//    }


    var indexInputLanguageSeleted: LiveData<Int> = _indexSrcItemLanguageSelected
    var indexOutputSelected: LiveData<Int> = _indexDesItemLanguageSelected

//    var intputLanguageSeleteted: LiveData<String> = _intputLanguageSeleteted
//    var outputLanguageSeleteted: LiveData<String> = _outputLanguageSeleteted

    fun updateSrcIndexLanguageSelectedValue(position: Int) {
        mRepo.updateLanguageInputIndex(position)
        _indexSrcItemLanguageSelected.value = position
    }

    fun updateDesIndexLanguageSelectedValue(position: Int) {
        mRepo.updateLanguageOutputIndex(position)
        _indexDesItemLanguageSelected.value = position
    }

    private var _translateResponse = MutableLiveData<TranslateItems>()
    var translateResponse:LiveData<TranslateItems> = _translateResponse

    fun translate(paragraph: String) {
        mRepo.translate(paragraph, getLanguageOutEntry()){
            Log.d("test", "translate:${it.textOut1} ")
            _translateResponse.value = it
        }
    }

    fun getLanguageInput() =  listCountry[indexInputLanguageSeleted.value!!]
    fun getLanguageOutput() = listCountry[indexOutputSelected.value!!]
    fun getLanguageInputEntry() = listCountryEntry[indexInputLanguageSeleted.value!!]
    fun getLanguageOutEntry() = listCountryEntry[indexOutputSelected.value!!]

    private var _autoDetectLanguageInput = MutableLiveData<Boolean>().apply {
        value = mRepo.autoDetectEnable()
    }
    var languageDetetectEnable: LiveData<Boolean> = _autoDetectLanguageInput
    fun setDetectInput(enable: Boolean) {
        _autoDetectLanguageInput.value = enable
        mRepo.setAutoDetect(enable)
    }

    fun clearText() {
        _translateResponse.value = TranslateItems()
    }
    fun firstSetUpLanguageSuccessed(){
        mRepo.firstSetUpSelectLanguageDone()
    }
    fun isFirstSetUpLanguageSucess(callback:(isDone:Boolean)->Unit){
        mRepo.getFirstSetUpSelectLanguageSuccess {
            callback(it)
        }
    }

    fun onTapTptranslateCofigChange(checked: Boolean) {
        mRepo.onTabToTranslateCofigChange(checked)
    }
    fun checkTapTranslateEnable(callback: (isDone: Boolean) -> Unit){
        mRepo.checkTapTranslateEnable{
            callback(it)
        }
    }

    var textTemp=""

    fun setServiceInstance(service: TapToTranslateService){
        mRepo.service=service
    }




    fun startCollect(){
        mRepo.setClipBoardListener()
        mCoroutine.launch {
            mRepo.clipBoardData?.collect {
               withContext(Main){
               clipboardData.value=it

               }

            }
        }
    }
    val clipboardData=MutableLiveData("")

    class GGTranslateViewModelFactory(
        private val respository: Repository,
        private var sharePreference: SharedPreferences,
        private val listCountry: Array<String>,
        private val listCountryEntry: Array<String>
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GGTranslateViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return GGTranslateViewModel(
                    respository, sharePreference, listCountry, listCountryEntry
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")

        }
    }
}