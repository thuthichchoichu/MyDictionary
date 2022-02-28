package com.visualpro.dictionary.interfaces

interface CallbackSoundLoaded {
    fun onSoundLoadComplete(soundType:String)
    fun onSoundLoadFail(soundType:String)
}
