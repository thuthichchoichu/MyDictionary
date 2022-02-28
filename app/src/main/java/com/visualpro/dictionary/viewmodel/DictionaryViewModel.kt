package com.visualpro.dictionary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.visualpro.dictionary.repository.Repository


class DictionaryViewModel(private var mRepo: Repository) : ViewModel(){
    var mRecentItems = mRepo.recentItems.asLiveData()
    var DailyWord= mRepo.dailyWord.asLiveData()


    fun checkDailyWord(){
        mRepo.getDailyWord()
    }
    class DictionaryViewModelFactory(
        private val respository: Repository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DictionaryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return DictionaryViewModel(respository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")

        }
    }
}




