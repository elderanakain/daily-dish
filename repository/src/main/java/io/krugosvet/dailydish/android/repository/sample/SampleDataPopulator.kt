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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_DATE
import java.time.temporal.ChronoUnit.MILLIS
import kotlin.random.Random

private const val FIVE_YEARS = 157784760000L
private const val RANDOM_DATE_SEED = 42

class SampleDataPopulator(
  private val mealFactory: MealFactory,
  private val resourceService: ResourceService
) :
  RoomDatabase.Callback(),
  KoinComponent {

  private val random = Random(RANDOM_DATE_SEED)

  private val randomDate: String
    get() = LocalDateTime.now().minus(random.nextLong(FIVE_YEARS), MILLIS).format(ISO_DATE)

  private val sampleData by lazy {
    listOf(
      mealFactory.create(
        title = resourceService.getString(R.string.sample_caesar_title),
        description = resourceService.getString(R.string.sample_caesar_description),
        date = randomDate,
        mainImage = MealImage(resourceService.createUri(R.drawable.sample_caesar_salad_image))
      ),
      mealFactory.create(
        title = resourceService.getString(R.string.sample_mushroom_soup_title),
        description = resourceService.getString(R.string.sample_mushroom_soup_description),
        date = randomDate,
        mainImage = MealImage(resourceService.createUri(R.drawable.sample_mushroom_soup_image))
      ),
      mealFactory.create(
        title = resourceService.getString(R.string.sample_caramel_rolls_title),
        description = resourceService.getString(R.string.sample_caramel_rolls_description),
        date = randomDate,
        mainImage = MealImage(resourceService.createUri(R.drawable.sample_caramel_rolls_image))
      ),
      mealFactory.create(
        title = resourceService.getString(R.string.sample_grilled_pizza_title),
        description = resourceService.getString(R.string.sample_grilled_pizza_description),
        date = randomDate,
        mainImage = MealImage(resourceService.createUri(R.drawable.sample_grilled_pizza_image))
      ),
      mealFactory.create(
        title = resourceService.getString(R.string.sample_burrito_title),
        description = resourceService.getString(R.string.sample_burrito_description),
        date = randomDate,
        mainImage = MealImage(resourceService.createUri(R.drawable.sample_grilled_pizza_image))
      ),
      mealFactory.create(
        title = resourceService.getString(R.string.sample_cheesecake_title),
        description = resourceService.getString(R.string.sample_cheesecake_description),
        date = randomDate,
        mainImage = MealImage(resourceService.createUri(R.drawable.sample_cheesecake_image))
      ),
      mealFactory.create(
        title = resourceService.getString(R.string.sample_chili_title),
        description = resourceService.getString(R.string.sample_cheesecake_description),
        date = randomDate,
        mainImage = MealImage(resourceService.createUri(R.drawable.sample_chili_image))
      ),
      mealFactory.create(
        title = resourceService.getString(R.string.sample_french_dip_title),
        description = resourceService.getString(R.string.sample_french_dip_description),
        date = randomDate,
        mainImage = MealImage(resourceService.createUri(R.drawable.sample_french_dip_image))
      ),
      mealFactory.create(
        title = resourceService.getString(R.string.sample_loin_roast_title),
        description = resourceService.getString(R.string.sample_loin_roast_description),
        date = randomDate,
        mainImage = MealImage(resourceService.createUri(R.drawable.sample_loin_roast_image))
      ),
      mealFactory.create(
        title = resourceService.getString(R.string.sample_meatloaf_title),
        description = resourceService.getString(R.string.sample_meatloaf_description),
        date = randomDate,
        mainImage = MealImage(resourceService.createUri(R.drawable.sample_meatloaf_image))
      ),
      mealFactory.create(
        title = resourceService.getString(R.string.sample_pork_chop_title),
        description = resourceService.getString(R.string.sample_pork_chop_description),
        date = randomDate,
        mainImage = MealImage(resourceService.createUri(R.drawable.sample_pork_chop_image))
      ),
      mealFactory.create(
        title = resourceService.getString(R.string.sample_ramen_title),
        description = resourceService.getString(R.string.sample_ramen_description),
        date = randomDate,
        mainImage = MealImage(resourceService.createUri(R.drawable.sample_ramen_image))
      ),
      mealFactory.create(
        title = resourceService.getString(R.string.sample_shrimp_skillet_title),
        description = resourceService.getString(R.string.sample_shrimp_skillet_description),
        date = randomDate,
        mainImage = MealImage(resourceService.createUri(R.drawable.sample_shrimp_skillet_image))
      ),
      mealFactory.create(
        title = resourceService.getString(R.string.sample_tacos_title),
        description = resourceService.getString(R.string.sample_tacos_description),
        date = randomDate,
        mainImage = MealImage(resourceService.createUri(R.drawable.sample_tacos_image))
      ),
      mealFactory.create(
        title = resourceService.getString(R.string.sample_teriyaki_title),
        description = resourceService.getString(R.string.sample_teriyaki_description),
        date = randomDate,
        mainImage = MealImage(resourceService.createUri(R.drawable.sample_teriyaki_image))
      ),
    )
  }

  override fun onCreate(db: SupportSQLiteDatabase) {
    super.onCreate(db)

    GlobalScope.launch(Dispatchers.IO) {
      get<MealRepository>().add(sampleData)
    }
  }
}
