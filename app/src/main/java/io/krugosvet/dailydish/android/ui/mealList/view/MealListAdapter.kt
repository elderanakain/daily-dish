package io.krugosvet.dailydish.android.ui.mealList.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.krugosvet.dailydish.android.databinding.ListMealBinding

class MealListAdapter(
  private val lifecycleOwner: LifecycleOwner
) :
  ListAdapter<MealVisual, MealListAdapter.MealViewHolder>(MealDiffUtilCallback) {

  init {
    setHasStableIds(true)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    MealViewHolder.from(parent, lifecycleOwner)

  override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
    holder.visualLiveData.value = getItem(position)
  }

  override fun getItemId(position: Int) = getItem(position).id

  class MealViewHolder private constructor(binding: ListMealBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {

      fun from(parent: ViewGroup, lifecycleOwner: LifecycleOwner): MealViewHolder {
        return MealViewHolder(
          ListMealBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            this.lifecycleOwner = lifecycleOwner
          }
        )
      }
    }

    val visualLiveData = MutableLiveData<MealVisual>()

    init {
      binding.visual = visualLiveData
    }
  }
}

object MealDiffUtilCallback : DiffUtil.ItemCallback<MealVisual>() {

  override fun areItemsTheSame(oldItem: MealVisual, newItem: MealVisual) = oldItem.id == newItem.id

  override fun areContentsTheSame(oldItem: MealVisual, newItem: MealVisual) = oldItem == newItem
}
