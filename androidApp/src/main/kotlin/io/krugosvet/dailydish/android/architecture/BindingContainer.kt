package io.krugosvet.dailydish.android.architecture

import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import io.krugosvet.dailydish.android.BR

interface BindingContainer<TBinding : ViewDataBinding, TViewModel : ViewModel> : LifecycleOwner {

    val bindingComponent: Binding<TBinding>

    fun onBind() = Unit

    val viewModel: TViewModel

    val binding: TBinding
        get() = bindingComponent.binding
}

/**
 * Represents binding part of UI layer
 */
interface Binding<TBinding : ViewDataBinding> {

    @get:LayoutRes
    val layoutId: Int

    val binding: TBinding
}

class BindingImpl<TBinding : ViewDataBinding, TViewModel : ViewModel>(
    @LayoutRes
    override val layoutId: Int,
    private val container: BindingContainer<TBinding, TViewModel>,
) :
    Binding<TBinding>,
    DefaultLifecycleObserver {

    override val binding: TBinding
        get() = _binding ?: error("Binding is not ready")

    private var _binding: TBinding? = null

    init {
        container.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        val context = when (container) {
            is AppCompatActivity -> container
            is Fragment -> container.requireContext()
            else -> error("Context is not available")
        }

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
