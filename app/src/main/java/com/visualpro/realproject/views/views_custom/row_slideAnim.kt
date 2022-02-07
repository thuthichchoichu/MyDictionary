package com.visualpro.realproject.views.views_custom

import android.view.View
import android.view.animation.TranslateAnimation

object row_Slide {
    val BASE=-15
    fun animRow(itemView: View, pos:Int){
        var xDelta=pos* BASE*10
        if(xDelta>500){
//            xDelta=500
        }
        val anim=TranslateAnimation(xDelta.toFloat(),0f,0f,0f)
        anim.duration=600
        itemView.startAnimation(anim)

    }


}