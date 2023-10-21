package io.krugosvet.dailydish.android.ui.mealList

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.krugosvet.dailydish.android.R

class MealListDecorator(
    private val resources: Resources,
) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)

        if (position == 0) {
            return
        }

        outRect.set(
            0,
            resources.getDimensionPixelSize(R.dimen.meal_list_offset),
            0,
            when {
                isLast(position, parent) -> {
                    resources.getDimensionPixelSize(R.dimen.meal_list_offset)
                }
                else -> 0
            },
        )
    }

    private fun isLast(position: Int, parent: RecyclerView): Boolean =
        position == parent.adapter!!.itemCount - 1
}
