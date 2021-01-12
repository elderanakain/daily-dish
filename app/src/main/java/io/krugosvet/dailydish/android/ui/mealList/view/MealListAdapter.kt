package io.krugosvet.dailydish.android.ui.mealList.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.krugosvet.dailydish.android.databinding.ListMealBinding

class MealListAdapter :
  PagingDataAdapter<MealVisual, MealListAdapter.MealViewHolder>(MealDiffUtilCallback) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    MealViewHolder.from(parent)

  override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
    holder.bind(getItem(position) ?: return)
  }

  class MealViewHolder private constructor(
    val binding: ListMealBinding
  ) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {

      fun from(parent: ViewGroup): MealViewHolder =
        MealViewHolder(
          ListMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    fun bind(visual: MealVisual) {
      binding.visual = visual
    }
  }
}

object MealDiffUtilCallback : DiffUtil.ItemCallback<MealVisual>() {

  override fun areItemsTheSame(oldItem: MealVisual, newItem: MealVisual) = oldItem.id == newItem.id

  override fun areContentsTheSame(oldItem: MealVisual, newItem: MealVisual) = oldItem == newItem
}
