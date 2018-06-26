package io.krugosvet.dailydish.android.mainScreen

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import io.krugosvet.dailydish.android.DailyDishApplication
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.db.objects.meal.Meal
import io.krugosvet.dailydish.android.ibm.appId.AuthTokenManager
import io.krugosvet.dailydish.android.utils.addListener
import io.krugosvet.dailydish.android.utils.getLongFormattedDate
import io.krugosvet.dailydish.android.utils.image.withNoCache
import io.krugosvet.dailydish.android.utils.intent.CameraImagePipe
import io.krugosvet.dailydish.android.utils.removeListener
import io.reactivex.Observable
import io.realm.*
import javax.inject.Inject

interface MealListAdapterPipe {
    fun deleteMeal(meal: Meal)
    fun onMealListChange(isEmpty: Boolean)
    fun changeMealMainImage(meal: Meal, image: Uri)
    fun removeMealMainImage(meal: Meal)
}

@Suppress("ProtectedInFinal")
class MealListAdapter(private val realm: Realm,
                      private val cameraImagePipe: CameraImagePipe,
                      private val query: () -> RealmQuery<Meal>,
                      private val mealListAdapterPipe: MealListAdapterPipe) :
        RealmRecyclerViewAdapter<Meal, MealListAdapter.MealViewHolder>(null, true) {

    @Inject
    protected lateinit var authTokenManager: AuthTokenManager
    @Inject
    protected lateinit var accountStateChangeReceiver: Observable<Intent>

    private val mealResults = query.invoke().findAll()
    private val mealListChangeListener = RealmChangeListener<RealmResults<Meal>> {
        mealListAdapterPipe.onMealListChange(isAdapterEmpty())
    }
    private var mealsToShow: Int = 0
    private var mealsToHide: Int = 0

    init {
        DailyDishApplication.appComponent.inject(this)
        setHasStableIds(true)
        updateData(mealResults)
        accountStateChangeReceiver.subscribe {
            updateData(query.invoke().findAll())
        }
    }

    fun setMealsToShow(mealsToShow: Int) {
        this.mealsToShow = mealsToShow
        notifyDataSetChanged()
    }

    fun setMealsToHide(mealsToHide: Int) {
        this.mealsToHide = mealsToHide
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MealViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_meal, parent, false))

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = data?.get(position)
        if (meal != null) holder.bind(meal)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id?.toLong() ?: 0
    }

    override fun updateData(data: OrderedRealmCollection<Meal>?) {
        removeListener(mealListChangeListener)
        super.updateData(data)
        addListener(mealListChangeListener)
        mealListAdapterPipe.onMealListChange(isAdapterEmpty())
    }

    override fun getItemCount(): Int {
        val dataCount = super.getItemCount()
        if (dataCount == 0) return dataCount
        if (mealsToShow > 0) return if (mealsToShow <= dataCount) mealsToShow else dataCount
        if (mealsToHide > 0) return if (dataCount - mealsToHide >= 0) dataCount - mealsToHide else 0
        return dataCount
    }

    private fun isAdapterEmpty() = itemCount == 0 || data?.isEmpty() ?: true

    inner class MealViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.title)
        private val description = view.findViewById<TextView>(R.id.description)
        private val deleteButton = view.findViewById<Button>(R.id.delete)
        private val lastDateOfCooking = view.findViewById<TextView>(R.id.last_date_of_cooking)
        private val mealImage = view.findViewById<ImageView>(R.id.meal_image)

        fun bind(meal: Meal) {
            title.text = meal.title
            description.text = meal.description
            lastDateOfCooking.text = getLongFormattedDate(meal.date)

            bindDeleteButton(meal)

            Glide.with(mealImage).applyDefaultRequestOptions(withNoCache().centerCrop())
                    .load(if (meal.mainImage.isEmpty()) R.drawable.food_clock_bw_800px else meal.mainImage)
                    .into(mealImage)

            mealImage.setOnClickListener {
                cameraImagePipe.openMealMainImageUpdateDialog({ image ->
                    mealListAdapterPipe.changeMealMainImage(meal, image)
                }, { mealListAdapterPipe.removeMealMainImage(meal) }, meal.mainImage.isEmpty())
            }
        }

        private fun bindDeleteButton(meal: Meal) {
            if (authTokenManager.isUserIdentified()) {
                deleteButton.visibility = View.VISIBLE
                deleteButton.setOnClickListener { mealListAdapterPipe.deleteMeal(meal) }
            } else deleteButton.visibility = View.GONE
        }
    }
}
