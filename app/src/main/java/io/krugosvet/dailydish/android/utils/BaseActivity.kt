package io.krugosvet.dailydish.android.utils

import android.view.Menu
import android.view.MenuItem
import io.krugosvet.dailydish.android.R

abstract class BaseActivity : RealmActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            if (item.itemId == R.id.action_login) {
                true
            } else super.onOptionsItemSelected(item)
}
