package com.visualpro.realproject.interfaces

interface CallbackSoundLoaded {
    fun onSoundLoadComplete(soundType:String)
    fun onSoundLoadFail(soundType:String)
}
