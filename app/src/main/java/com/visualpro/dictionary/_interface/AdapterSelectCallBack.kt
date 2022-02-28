package com.visualpro.dictionary._interface

interface AdapterSelectCallBack {
    fun languageSelect(position:Int,)
    fun noResultFound(noResultFound:Boolean)
    fun scrollToPosition(position: Int)
}