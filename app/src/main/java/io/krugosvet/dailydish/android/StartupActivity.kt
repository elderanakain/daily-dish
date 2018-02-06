package io.krugosvet.dailydish.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.krugosvet.dailydish.android.db.objects.Meal
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_startup.*

class StartupActivity : AppCompatActivity(), DialogAddMeal.DialogAddMealListener {

    private val realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)
        mealList.adapter = MealListAdapter(realm.where(Meal::class.java).findAll())

        floatingButton.setOnClickListener {
            DialogAddMeal().show(supportFragmentManager, "")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onAddButtonClick(mealTitle: String, mealDescription: String) {
        Meal(mealTitle, mealDescription).persist(realm)
    }
}
