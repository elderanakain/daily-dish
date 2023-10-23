package io.krugosvet.dailydish.android.ui.mealList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.krugosvet.dailydish.android.databinding.ListMealBinding

class MealListAdapter :
    ListAdapter<MealVisual, MealListAdapter.MealViewHolder>(MealDiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MealViewHolder.from(parent)

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    class MealViewHolder(val binding: ListMealBinding) :
        ViewHolder(binding.root) {

        fun bind(visual: MealVisual) {
            binding.visual = visual
        }

        companion object {
            fun from(parent: ViewGroup): MealViewHolder =
                MealViewHolder(
                    ListMealBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                )
        }
    }
}

object MealDiffUtilCallback : DiffUtil.ItemCallback<MealVisual>() {

    override fun areItemsTheSame(oldItem: MealVisual, newItem: MealVisual) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MealVisual, newItem: MealVisual) = oldItem == newItem
}
