package io.krugosvet.dailydish.android.mainScreen

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import io.krugosvet.dailydish.android.DailyDishApplication
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.db.objects.meal.Meal
import io.krugosvet.dailydish.android.utils.addListener
import io.krugosvet.dailydish.android.utils.getLongFormattedDate
import io.krugosvet.dailydish.android.utils.image.loadMealMainImage
import io.krugosvet.dailydish.android.utils.intent.CameraImagePipe
import io.krugosvet.dailydish.android.utils.isCurrentDate
import io.krugosvet.dailydish.android.utils.removeListener
import io.realm.*
import io.realm.kotlin.isValid

interface MealListAdapterPipe {

  fun deleteMeal(meal: Meal)

  fun onMealListChange(isEmpty: Boolean)

  fun changeMealMainImage(meal: Meal, image: Uri)

  fun removeMealMainImage(meal: Meal)

  fun showLongSnackbar(@StringRes message: Int)

  fun changeMealCookedDate(meal: Meal)

}

@Suppress("ProtectedInFinal")
class MealListAdapter(
  private val cameraImagePipe: CameraImagePipe,
  query: () -> RealmQuery<Meal>,
  private val mealListAdapterPipe: MealListAdapterPipe
) :
  RealmRecyclerViewAdapter<Meal, MealListAdapter.MealViewHolder>(null, true) {

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

    updateData(query.invoke().findAll())
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
    MealViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.list_meal, parent, false)
    )

  override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
    val meal = data?.get(position)

    if (meal != null) {
      holder.bind(meal)
    }
  }

  override fun getItemId(position: Int): Long = getItem(position)?.id?.toLong() ?: 0

  override fun updateData(data: OrderedRealmCollection<Meal>?) {
    removeListener(mealListChangeListener)
    super.updateData(data)
    addListener(mealListChangeListener)
    mealListAdapterPipe.onMealListChange(isAdapterEmpty())
  }

  override fun getItemCount(): Int {
    val dataCount = super.getItemCount()

    return when {
      dataCount == 0 -> dataCount
      mealsToShow > 0 -> if (mealsToShow <= dataCount) mealsToShow else dataCount
      mealsToHide > 0 -> if (dataCount - mealsToHide >= 0) dataCount - mealsToHide else 0
      else -> dataCount
    }
  }

  private fun isAdapterEmpty() = itemCount == 0 || data?.isEmpty() ?: true

  inner class MealViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title = view.findViewById<TextView>(R.id.title)
    private val description = view.findViewById<TextView>(R.id.description)
    private val deleteButton = view.findViewById<Button>(R.id.delete)
    private val lastDateOfCooking = view.findViewById<TextView>(R.id.last_date_of_cooking)
    private val mealImage = view.findViewById<ImageView>(R.id.meal_image)
    private val cookedTodayButton = view.findViewById<Button>(R.id.cookedButton)
    private val expandCardButton = view.findViewById<ImageView>(R.id.expandCardButton)

    private var isExpanded = false

    fun bind(meal: Meal) {
      title.text = meal.title
      description.text = meal.description
      lastDateOfCooking.text = lastDateOfCooking.context
        .getString(R.string.cooked_on, getLongFormattedDate(meal.date))

      bindDeleteButton(meal)
      bindMealMainImage(meal)
      bindCookedTodayButton(meal)

      expandCardButton.setOnClickListener {
        if (isExpanded) {
          expandCardButton.setImageResource(R.drawable.meal_expand_arrow)
          description.maxLines = 3
          isExpanded = false
        } else {
          expandCardButton.setImageResource(R.drawable.meal_collapse_arrow)
          description.maxLines = Integer.MAX_VALUE
          isExpanded = true
        }
      }
    }

    private fun bindCookedTodayButton(meal: Meal) {
      if (true) {
        cookedTodayButton.visibility = View.VISIBLE
        if (!isCurrentDate(meal.date)) {
          cookedTodayButton.isEnabled = true
          cookedTodayButton.setOnClickListener {
            mealListAdapterPipe.changeMealCookedDate(meal)
          }
        } else {
          cookedTodayButton.isEnabled = false
        }
      } else {
        cookedTodayButton.visibility = View.GONE
      }

    }

    private fun bindDeleteButton(meal: Meal) {
      if (true) {
        deleteButton.visibility = View.VISIBLE
        deleteButton.setOnClickListener { if (meal.isValid()) mealListAdapterPipe.deleteMeal(meal) }
      } else deleteButton.visibility = View.GONE
    }

    private fun bindMealMainImage(meal: Meal) {
      loadMealMainImage(mealImage, meal.mainImage)

      mealImage.setOnClickListener {
        if (true) {
          cameraImagePipe.openMealMainImageUpdateDialog({ image ->
            mealListAdapterPipe.changeMealMainImage(meal, image)
          }, { mealListAdapterPipe.removeMealMainImage(meal) }, meal.mainImage.isEmpty())
        } else {
          mealListAdapterPipe.showLongSnackbar(R.string.not_auth_update_meal_image_error)
        }
      }
    }
  }
}
