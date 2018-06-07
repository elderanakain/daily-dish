package io.krugosvet.dailydish.android.network

import android.content.Context
import android.widget.Toast
import io.krugosvet.dailydish.android.db.objects.Meal
import io.krugosvet.dailydish.android.ibm.appId.AuthTokenManager
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import retrofit2.http.*

const val MEAL_ENDPOINT = "meal.php"

interface MealService {

    @GET(MEAL_ENDPOINT)
    fun getMeals(@Query("userId") user: String): Single<List<Meal>>

    @FormUrlEncoded
    @POST(MEAL_ENDPOINT)
    fun sendMeal(@Body meal: Meal): Completable
}

interface MealServicePipe {
    fun getMeals(onSuccess: (meals: List<Meal>) -> Unit)
}

class MealServicePipeImpl(private val appContext: Context, private val mealService: MealService,
                          private val authTokenManager: AuthTokenManager) : MealServicePipe {

    override fun getMeals(onSuccess: (meals: List<Meal>) -> Unit) {
        mealService.getMeals(authTokenManager.userId()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Meal>>() {
                    override fun onSuccess(meals: List<Meal>) {
                        onSuccess.invoke(meals)
                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(appContext, e.message, Toast.LENGTH_LONG).show()
                    }
                })
    }
}
