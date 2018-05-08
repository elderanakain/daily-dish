package io.krugosvet.dailydish.android

import android.app.Application
import com.ibm.bluemix.appid.android.api.AppID
import io.realm.Realm

class DailyDishApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        AppID.getInstance().initialize(this, resources.getString(R.string.authTenantId), AppID.REGION_UK);
    }
}
