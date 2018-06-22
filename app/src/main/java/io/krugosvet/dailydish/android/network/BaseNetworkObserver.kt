package io.krugosvet.dailydish.android.network

import android.support.annotation.StringRes
import android.view.View
import io.krugosvet.dailydish.android.BuildConfig
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.utils.baseUi.BaseActivity
import io.krugosvet.dailydish.android.utils.baseUi.showLongSnackbar
import io.reactivex.MaybeObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import java.net.UnknownHostException
import javax.annotation.OverridingMethodsMustInvokeSuper

abstract class BaseNetworkObserver<T>(private val baseActivity: BaseActivity) : MaybeObserver<T>,
        SingleObserver<T> {

    protected abstract val onErrorMessage: Int

    private val progressBar = baseActivity.getProgressBar()

    @OverridingMethodsMustInvokeSuper
    override fun onSubscribe(d: Disposable) {
        progressBar?.visibility = View.VISIBLE
    }

    override fun onError(e: Throwable) {
        if (BuildConfig.DEBUG) e.printStackTrace()

        if (e is UnknownHostException && !baseActivity.isInternetConnection()) {
            onFinish(R.string.network_no_internet_connection)
        } else onFinish(onErrorMessage)
    }

    override fun onComplete() {}

    protected open fun onFinish(@StringRes messageId: Int) {
        progressBar?.visibility = View.INVISIBLE
        showLongSnackbar(baseActivity, messageId)
    }
}
