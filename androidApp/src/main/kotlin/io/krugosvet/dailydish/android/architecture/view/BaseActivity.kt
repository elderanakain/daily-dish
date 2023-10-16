package io.krugosvet.dailydish.android.architecture.view

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.aspect.DisposableAspect
import io.krugosvet.dailydish.android.architecture.aspect.IBindingContainer
import io.krugosvet.dailydish.android.architecture.aspect.IStorageAspect
import io.krugosvet.dailydish.android.architecture.extension.RequestPermissionWithCallback
import io.krugosvet.dailydish.android.architecture.extension.RequestPermissionWithCallback.Input
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.krugosvet.dailydish.android.errorHandler
import io.krugosvet.dailydish.android.service.permission.Permission
import io.reactivex.disposables.Disposable
import kotlin.coroutines.resume
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.plus
import kotlinx.coroutines.suspendCancellableCoroutine

abstract class BaseActivity<TBinding : ViewDataBinding, TViewModel : ViewModel<*>> :
    AppCompatActivity(),
    IBindingContainer<TBinding, TViewModel>,
    IStorageAspect<Disposable> by DisposableAspect() {

    abstract override val viewModel: TViewModel

    protected val navController: NavController by lazy { findNavController(R.id.hostFragment) }

    private val permissionRequester = registerForActivityResult(RequestPermissionWithCallback()) {}

    suspend fun requestPermission(permission: Permission): Boolean = suspendCancellableCoroutine {
        val input = Input(permission) { isGranted -> it.resume(isGranted) }

        permissionRequester.launch(input)
    }

    override fun onDestroy() {
        super.onDestroy()

        clear()
    }

    protected fun <T> Flow<T>.launchInCatching(): Job =
        launchIn(lifecycleScope + errorHandler)
}
