package io.krugosvet.dailydish.android

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.krugosvet.dailydish.android.db.objects.Meal
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class MealListAdapter(private val mealList: OrderedRealmCollection<Meal>)
    : RealmRecyclerViewAdapter<Meal, MealListAdapter.MealViewHolder>(mealList,true) {

   init {
       setHasStableIds(true)
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MealViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_meal, parent, false))

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(mealList[position])
    }

    inner class MealViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.title)
        private val description = view.findViewById<TextView>(R.id.description)

        fun bind(meal: Meal) {
            title.text = meal.title
            description.text = meal.description
        }
    }
}
