package com.visualpro.dictionary.ui.views_custom

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewDisable:RecyclerView.OnItemTouchListener {
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent)=true

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }
}