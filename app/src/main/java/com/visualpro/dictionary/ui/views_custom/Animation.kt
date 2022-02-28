package com.visualpro.dictionary.views.views_custom

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation


object Animation {
    val anim=TranslateAnimation(500f,0f,0f,0f).apply {
        duration=300
    }
    val animation1 = AlphaAnimation(0.0f, 1.0f).apply {
        setDuration(800);
       setFillAfter(true)
    }
    fun animRow(itemView: View){
        val set=AnimationSet(true)
        set.addAnimation(animation1)
        set.addAnimation(anim)
        itemView.startAnimation(set)


    }
    fun animRow2(view:View){
        view.startAnimation(animation1)
    }

    val animation2=RotateAnimation(-5f, 5f, 50f, 50f).apply {
        repeatCount=100
        duration=300
    }
    fun shakeAnimation(view: View) {

      view.startAnimation(animation2)
    }
}