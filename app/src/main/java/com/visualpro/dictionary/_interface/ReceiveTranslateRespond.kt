package com.visualpro.dictionary._interface

import com.visualpro.dictionary.model.TranslateItems

interface ReceiveTranslateRespond {
    fun translateResponse(string:TranslateItems)
}