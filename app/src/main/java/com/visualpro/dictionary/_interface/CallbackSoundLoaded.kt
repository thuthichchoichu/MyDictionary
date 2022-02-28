package com.visualpro.dictionary._interface

interface CallbackSoundLoaded {
    fun onSoundLoadComplete(soundType:String)
    fun onSoundLoadFail(soundType:String)
}
