package io.krugosvet.dailydish.android.architecture.aspect

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import io.krugosvet.dailydish.android.BR

interface IBindingContainer<TBinding : ViewDataBinding, TViewModel : ViewModel> : LifecycleOwner {

    val bindingComponent: IBindingComponent<TBinding>

    fun onBind() = Unit

    val parentContext: Context

    val viewModel: TViewModel

    val binding: TBinding
        get() = bindingComponent.binding
}

/**
 * Represents binding part of UI layer
 */
interface IBindingComponent<TBinding : ViewDataBinding> {

    @get:LayoutRes
    val layoutId: Int

    val binding: TBinding
}

class BindingComponent<TBinding : ViewDataBinding, TViewModel : ViewModel>(
    @LayoutRes
    override val layoutId: Int,
    private val container: IBindingContainer<TBinding, TViewModel>,
) :
    IBindingComponent<TBinding>,
    DefaultLifecycleObserver {

    override val binding: TBinding
        get() = _binding ?: throw IllegalStateException("Binding is not ready")

    private var _binding: TBinding? = null

    init {
        container.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        val context = container.parentContext

        _binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, null, false)
        binding.lifecycleOwner = container

        if (container is AppCompatActivity) {
            container.setContentView(binding.root)
        }

        container.onBind()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        binding.setVariable(BR.viewModel, container.viewModel)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

        _binding = null
    }
}
