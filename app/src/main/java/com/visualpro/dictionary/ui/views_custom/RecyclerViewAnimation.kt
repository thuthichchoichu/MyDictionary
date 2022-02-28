package com.visualpro.dictionary.ui.views_custom

import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.Transformation

object RecyclerViewAnimation {
    fun toggleArrow(view: View, isExpanded: Boolean): Boolean {
        return if (isExpanded) {
            view.animate().setDuration(100).rotation(180f)
            true
        } else {
            view.animate().setDuration(100).rotation(0f)
            false
        }
    }

    fun expand(view: View) {
      expandAction(view)

    }

    private fun expandAction(view: View) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val actualheight = view.measuredHeight
        view.layoutParams.height = 0
//        view.invalidate()
//        view.visibility = View.VISIBLE
//        Handler(Looper.getMainLooper()).postDelayed({
//            view.visibility = View.VISIBLE
//        },2100)

        val animation: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                view.layoutParams.height = if (interpolatedTime ==1f ) ViewGroup.LayoutParams.WRAP_CONTENT else (actualheight * interpolatedTime).toInt()
                view.requestLayout()
            }
        }
        animation.duration = 200
        val alpha=AlphaAnimation(0f,1f)
        alpha.duration=50
        val set=AnimationSet(true)
        set.addAnimation(animation)
        set.addAnimation(alpha)
        view.startAnimation(set)
    }

    fun collapse(view: View) {
        val actualHeight = view.measuredHeight

        val animation: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
//                    view.visibility = View.GONE
                } else {
                    view.layoutParams.height =
                        actualHeight - (actualHeight * interpolatedTime).toInt()
                    view.requestLayout()
                }
            }
        }
        animation.duration = 200
        val alpha=AlphaAnimation(1f,0f)
        alpha.duration=50
        val set=AnimationSet(true)
        set.addAnimation(animation)
        set.addAnimation(alpha)
        view.startAnimation(set)
    }
}
