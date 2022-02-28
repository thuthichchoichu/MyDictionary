package com.visualpro.dictionary.ui.views_custom

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewDisable:RecyclerView.OnItemTouchListener {
    var allowTouch=true
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent)=allowTouch

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }
}