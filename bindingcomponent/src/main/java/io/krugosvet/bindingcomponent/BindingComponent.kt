package io.krugosvet.bindingcomponent

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*

interface IBindingContainer<TBinding : ViewDataBinding, TViewModel : ViewModel> : LifecycleOwner {

  val bindingComponent: IBindingComponent<TBinding>

  fun onBind() = Unit

  val parentContext: Context

  val viewModel: TViewModel
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
  private val viewModelVar: Int
) :
  IBindingComponent<TBinding>,
  DefaultLifecycleObserver {

  override lateinit var binding: TBinding

  init {
    container.lifecycle.addObserver(this)
  }

  override fun onCreate(owner: LifecycleOwner) {
    super.onCreate(owner)

    val context = container.parentContext

    binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, null, false)
    binding.lifecycleOwner = container

    if (container is AppCompatActivity) {
      container.setContentView(binding.root)
    }

    container.onBind()
  }

  override fun onStart(owner: LifecycleOwner) {
    super.onStart(owner)

    binding.setVariable(viewModelVar, container.viewModel)
  }
}
