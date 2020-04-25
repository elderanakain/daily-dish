package io.krugosvet.dailydish.android.network

import android.app.*
import android.view.*
import androidx.annotation.*
import io.krugosvet.dailydish.android.*
import io.krugosvet.dailydish.android.utils.baseUi.*
import io.reactivex.*
import io.reactivex.disposables.*
import java.net.*
import javax.annotation.*

abstract class BaseNetworkObserver<T>(
    private val baseActivity: Activity?
) :
    MaybeObserver<T>,
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
    if (BuildConfig.DEBUG) {
      e.printStackTrace()
    }

    if (
        e is UnknownHostException
        && (baseActivity as? BaseActivity)?.isInternetConnection() == false
    ) {
      onFinish(R.string.network_no_internet_connection)
    } else {
      onFinish(onErrorMessage)
    }
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

    if (activeRequests != 0) {
      activeRequests--
    }

    toggleProgressBarVisibility()
  }

  private fun toggleProgressBarVisibility() {
    baseActivity?.runOnUiThread {
      progressBar?.visibility = if (activeRequests > 0) View.VISIBLE else View.INVISIBLE
    }
  }
}
