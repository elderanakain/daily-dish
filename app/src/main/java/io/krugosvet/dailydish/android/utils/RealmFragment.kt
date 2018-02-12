package io.krugosvet.dailydish.android.utils

import android.support.v4.app.Fragment

open class RealmFragment: Fragment() {

    protected fun getRealm() = (activity as RealmActivity).realm
}
