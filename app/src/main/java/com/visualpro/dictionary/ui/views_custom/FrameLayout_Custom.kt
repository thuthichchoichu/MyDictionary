package com.visualpro.dictionary.ui.views_custom

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import org.jetbrains.annotations.Nullable


class FrameLayout_Custom : FrameLayout {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {}
    constructor(
        context: Context,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    constructor(
        context: Context,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(heightMeasureSpec),
                MeasureSpec.EXACTLY
            )
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}