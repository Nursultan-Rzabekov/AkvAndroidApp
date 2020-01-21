package com.example.akvandroidapp.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EndSpacingItemDecoration (private val margin: Int) : RecyclerView.ItemDecoration() {

    companion object {
        val STANDARD_SPACING: Int = 50
    }
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = margin
    }
}