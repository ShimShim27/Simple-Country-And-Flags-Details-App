package com.countries.flag.ui.main

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CountriesRecyclerDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemPosition = parent.getChildAdapterPosition(view)
        outRect.left = 20
        outRect.right = 20
        outRect.top = if (itemPosition == 0) 30 else 10
        outRect.bottom = if (itemPosition >= parent.adapter!!.itemCount - 1) 40 else 0
    }
}