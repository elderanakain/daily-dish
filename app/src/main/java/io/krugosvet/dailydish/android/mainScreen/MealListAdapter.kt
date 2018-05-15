package io.krugosvet.dailydish.android.mainScreen

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.db.objects.Meal
import io.krugosvet.dailydish.android.utils.getFormattedDate
import io.krugosvet.dailydish.android.utils.image.withNoCache
import io.krugosvet.dailydish.android.utils.intent.CameraImagePipe
import io.reactivex.Observable
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.RealmRecyclerViewAdapter

open class MealListAdapter(private val realm: Realm,
                           private val cameraImagePipe: CameraImagePipe,
                           private val query: () -> RealmQuery<Meal>,
                           accountStateChangeReceiver: Observable<Intent>)
    : RealmRecyclerViewAdapter<Meal, MealListAdapter.MealViewHolder>(query.invoke().findAll(), true) {

    init {
        setHasStableIds(true)
        accountStateChangeReceiver.subscribe {
            updateData(query.invoke().findAll())
        }
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
        private val lastDateOfCooking = view.findViewById<TextView>(R.id.last_date_of_cooking)
        private val mealImage = view.findViewById<ImageView>(R.id.meal_image)

        fun bind(meal: Meal?) {
            title.text = meal?.title
            description.text = meal?.description
            lastDateOfCooking.text = getFormattedDate(meal?.date)
            deleteButton.setOnClickListener {
                meal?.delete(realm)
            }

            val mainImage = meal?.mainImage ?: byteArrayOf()
            Glide.with(mealImage).applyDefaultRequestOptions(withNoCache().centerCrop())
                    .load(if (mainImage.isEmpty()) R.drawable.food_clock_bw_800px else mainImage)
                    .into(mealImage)

            mealImage.setOnClickListener {
                cameraImagePipe.openMealMainImageUpdateDialog({ file ->
                    meal?.changeMainImage(realm, file)
                    notifyItemChanged(layoutPosition)
                    file?.delete()
                }, { meal?.removeMainImage(realm) }, meal?.mainImage?.isEmpty() ?: true)
            }
        }
    }
}
