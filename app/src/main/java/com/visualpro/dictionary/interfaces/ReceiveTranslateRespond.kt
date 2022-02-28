package com.visualpro.dictionary.interfaces

import com.visualpro.dictionary.model.TranslateItems

interface ReceiveTranslateRespond {
    fun translateResponse(string:TranslateItems)
}