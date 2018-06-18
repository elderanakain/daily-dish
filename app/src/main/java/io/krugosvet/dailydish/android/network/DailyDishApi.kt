package io.krugosvet.dailydish.android.network

import io.krugosvet.dailydish.android.db.objects.Meal
import io.krugosvet.dailydish.android.ibm.appId.AuthTokenManager
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.http.*

const val MEAL_ENDPOINT = "meal.php"

interface MealService {

    @GET(MEAL_ENDPOINT)
    fun getMeals(@Query("userId") user: String): Maybe<List<Meal>>

    @GET(MEAL_ENDPOINT)
    fun getMeals(): Maybe<List<Meal>>

    @FormUrlEncoded
    @POST(MEAL_ENDPOINT)
    fun sendMeal(@Body meal: Meal): Completable
}

interface MealServicePipe {
    fun getMeals(): Maybe<List<Meal>>
}

class MealServicePipeImpl(private val mealService: MealService,
                          private val authTokenManager: AuthTokenManager) : MealServicePipe {

    override fun getMeals(): Maybe<List<Meal>> = getMealObserver(authTokenManager.userId())
            .observeOn(AndroidSchedulers.mainThread())

    private fun getMealObserver(userId: String) =
            if (userId.isEmpty()) mealService.getMeals() else mealService.getMeals(userId)
}
