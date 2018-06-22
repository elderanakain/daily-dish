package io.krugosvet.dailydish.android.network

import io.krugosvet.dailydish.android.db.objects.Meal
import io.krugosvet.dailydish.android.ibm.appId.AuthTokenManager
import io.krugosvet.dailydish.android.network.json.MealId
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

const val MEAL_ENDPOINT = "meal.php"
const val USER_ID_QUERY = "userId"

interface MealService {

    @GET(MEAL_ENDPOINT)
    fun getMeals(@Query(USER_ID_QUERY) user: String): Maybe<List<Meal>>

    @GET(MEAL_ENDPOINT)
    fun getMeals(): Maybe<List<Meal>>

    @POST(MEAL_ENDPOINT)
    fun sendMeal(@Body meal: Meal): Single<MealId>
}

interface MealServicePipe {
    fun getMeals(): Maybe<List<Meal>>

    fun sendMeal(meal: Meal): Single<MealId>
}

class MealServicePipeImpl(private val mealService: MealService,
                          private val authTokenManager: AuthTokenManager) : MealServicePipe {

    override fun getMeals(): Maybe<List<Meal>> = getMealObserver(authTokenManager.userId())
            .observeOn(AndroidSchedulers.mainThread())

    override fun sendMeal(meal: Meal): Single<MealId> = mealService.sendMeal(meal)
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())

    private fun getMealObserver(userId: String) =
            if (userId.isEmpty()) mealService.getMeals() else mealService.getMeals(userId)
}
