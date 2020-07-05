package io.krugosvet.dailydish.android.repository.sample

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.krugosvet.dailydish.android.repository.R
import io.krugosvet.dailydish.android.repository.meal.MealFactory
import io.krugosvet.dailydish.android.repository.meal.MealImage
import io.krugosvet.dailydish.android.repository.meal.MealRepository
import io.krugosvet.dailydish.core.service.ResourceService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get

class SampleDataPopulator(
  private val mealFactory: MealFactory,
  private val resourceService: ResourceService
) :
  RoomDatabase.Callback(),
  KoinComponent {

  private val sampleData by lazy {
    listOf(
      mealFactory.create(
        title = resourceService.getString(R.string.sample_caesar_title),
        description = resourceService.getString(R.string.sample_caesar_description),
        date = "2020-03-12",
        mainImage = MealImage(resourceService.createUri(R.drawable.sample_caesar_salad_image))
      ),
      mealFactory.create(
        title = resourceService.getString(R.string.sample_mushroom_soup_title),
        description = resourceService.getString(R.string.sample_mushroom_soup_description),
        date = "2020-04-01",
        mainImage = MealImage(resourceService.createUri(R.drawable.sample_mushroom_soup_image))
      ),
      mealFactory.create(
        title = resourceService.getString(R.string.sample_caramel_rolls_title),
        description = resourceService.getString(R.string.sample_caramel_rolls_description),
        date = "2020-06-10",
        mainImage = MealImage(resourceService.createUri(R.drawable.sample_caramel_rolls_image))
      ),
      mealFactory.create(
        title = resourceService.getString(R.string.sample_grilled_pizza_title),
        description = resourceService.getString(R.string.sample_grilled_pizza_description),
        date = "2020-06-12",
        mainImage = MealImage(resourceService.createUri(R.drawable.sample_grilled_pizza_image))
      )
    )
  }

  override fun onCreate(db: SupportSQLiteDatabase) {
    super.onCreate(db)

    GlobalScope.launch(Dispatchers.IO) {
      get<MealRepository>().add(*sampleData.toTypedArray())
    }
  }

}
