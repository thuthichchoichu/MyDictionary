package com.visualpro.dictionary

import java.text.SimpleDateFormat
import java.util.*

class TimerWatch {
    companion object{
        fun getTime():String{
            val sdf: SimpleDateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
            return sdf.format(Date())
        }
    }
}