package io.krugosvet.dailydish.android.network

import android.app.Activity
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

abstract class BaseNetworkObserver<T>(private val baseActivity: Activity?) : MaybeObserver<T>,
        SingleObserver<T> {

    companion object {
        var activeRequests: Int = 0
    }

    protected abstract val onErrorMessage: Int
    protected abstract val onSuccessMessage: Int

    private val progressBar = (baseActivity as? BaseActivity)?.getProgressBar()

    @OverridingMethodsMustInvokeSuper
    override fun onSubscribe(d: Disposable) {
        activeRequests++
        toggleProgressBarVisibility()
    }

    @OverridingMethodsMustInvokeSuper
    override fun onError(e: Throwable) {
        if (BuildConfig.DEBUG) e.printStackTrace()

        if (e is UnknownHostException && (baseActivity as? BaseActivity)?.isInternetConnection() == false) {
            onFinish(R.string.network_no_internet_connection)
        } else onFinish(onErrorMessage)
    }

    @OverridingMethodsMustInvokeSuper
    override fun onComplete() {
        onFinish(onSuccessMessage)
    }

    @OverridingMethodsMustInvokeSuper
    override fun onSuccess(result: T) {
        onFinish(onSuccessMessage)
    }

    private fun onFinish(@StringRes messageId: Int) {
        showLongSnackbar((baseActivity as? BaseActivity), messageId)
        if (activeRequests != 0) activeRequests--
        toggleProgressBarVisibility()
    }

    private fun toggleProgressBarVisibility() {
        baseActivity?.runOnUiThread{
            progressBar?.visibility = if (activeRequests > 0) View.VISIBLE else View.INVISIBLE
        }
    }
}
