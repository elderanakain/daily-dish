package io.krugosvet.dailydish.android.mainScreen

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.db.objects.Meal
import io.krugosvet.dailydish.android.utils.getFormattedDate
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter

open class MealListAdapter(private val realm: Realm, items: OrderedRealmCollection<Meal>, private val limit: Int = NO_LIMIT)
    : RealmRecyclerViewAdapter<Meal, MealListAdapter.MealViewHolder>(items, true) {

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

    override fun getItemCount(): Int {
        return if (limit == NO_LIMIT || limit > super.getItemCount()) super.getItemCount() else limit
    }

    inner class MealViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.title)
        private val description = view.findViewById<TextView>(R.id.description)
        private val deleteButton = view.findViewById<Button>(R.id.delete)
        private val lastDateOfCooking = view.findViewById<TextView>(R.id.lastDateOfCooking)

        fun bind(meal: Meal?) {
            title.text = meal?.title
            description.text = meal?.description
            lastDateOfCooking.text = getFormattedDate(meal?.date)
            deleteButton.setOnClickListener {
                meal?.delete(realm)
            }
        }
    }
}
