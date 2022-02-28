package com.visualpro.dictionary.interfaces

interface AdapterSelectCallBack {
    fun languageSelect(position:Int,)
    fun noResultFound(noResultFound:Boolean)
    fun scrollToPosition(position: Int)
}