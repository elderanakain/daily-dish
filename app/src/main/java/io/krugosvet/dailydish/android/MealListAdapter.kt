package io.krugosvet.dailydish.android

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.krugosvet.dailydish.android.db.objects.Meal

class MealListAdapter(private var mealList: List<Meal>) : RecyclerView.Adapter<MealListAdapter.MealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MealViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_meal, parent, false))

    override fun getItemCount() = mealList.size

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(mealList[position])
    }

    inner class MealViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val description = view.findViewById<TextView>(R.id.description)

        fun bind(meal: Meal) {
            description.text = meal.description
        }
    }
}
