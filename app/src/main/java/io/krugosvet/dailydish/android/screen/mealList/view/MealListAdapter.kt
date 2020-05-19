package io.krugosvet.dailydish.android.screen.mealList.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import io.krugosvet.dailydish.android.databinding.ListMealBinding
import org.koin.core.KoinComponent
import kotlin.properties.Delegates.observable

class MealListAdapter(
  private val lifecycleOwner: LifecycleOwner
) :
  RecyclerView.Adapter<MealListAdapter.MealViewHolder>(),
  KoinComponent {

  var data: List<MealVisual> by observable(listOf()) { _, _, _ ->
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    MealViewHolder(
      ListMealBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
        lifecycleOwner = this@MealListAdapter.lifecycleOwner
      }
    )

  override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
    holder.visualLiveData.value = data[position]
  }

  override fun getItemCount() = data.size

  inner class MealViewHolder(binding: ListMealBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val visualLiveData = MutableLiveData<MealVisual>()

    init {
      binding.visual = visualLiveData
    }
  }
}
