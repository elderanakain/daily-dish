package io.krugosvet.dailydish.android

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import io.krugosvet.dailydish.android.db.objects.Meal
import io.krugosvet.dailydish.android.utils.getMeals
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import io.realm.kotlin.deleteFromRealm

class MealListAdapter(private val realm: Realm)
    : RealmRecyclerViewAdapter<Meal, MealListAdapter.MealViewHolder>(realm.getMeals(),true) {

   init {
       setHasStableIds(true)
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MealViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_meal, parent, false))

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(data?.get(position))
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id?.toLong() ?: 0
    }

    inner class MealViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.title)
        private val description = view.findViewById<TextView>(R.id.description)
        private val deleteButton = view.findViewById<Button>(R.id.delete)

        fun bind(meal: Meal?) {
            title.text = meal?.title
            description.text = meal?.description
            deleteButton.setOnClickListener {
                realm.executeTransaction {
                    meal?.deleteFromRealm()
                }
            }
        }
    }
}
