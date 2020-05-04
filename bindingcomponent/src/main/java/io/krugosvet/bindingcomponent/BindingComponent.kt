package io.krugosvet.bindingcomponent

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*

interface IBindingContainer<TBinding: ViewDataBinding, TVisual> : LifecycleOwner {

  val bindingComponent: IBindingComponent<TBinding, TVisual>

  fun onBind() = Unit

  val parentContext: Context
}

/**
 * Represents binding part of UI layer
 */
interface IBindingComponent<TBinding: ViewDataBinding, TVisual> {

  @get:LayoutRes
  val layoutId: Int

  /**
   * Required visual model for data binding
   */
  val visual: MutableLiveData<TVisual>

  val binding: TBinding
}

class BindingComponent<TBinding: ViewDataBinding, TVisual>(
  @LayoutRes
  override val layoutId: Int,
  private val container: IBindingContainer<TBinding, TVisual>,
  private val visualVar: Int
) :
  IBindingComponent<TBinding, TVisual>,
  LifecycleObserver {

  override val visual = MutableLiveData<TVisual>()

  override lateinit var binding: TBinding

  init {
    container.lifecycle.addObserver(this)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  private fun createBinding() {
    val context = container.parentContext

    binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, null, false)
    binding.lifecycleOwner = container

    if (container is AppCompatActivity) {
      container.setContentView(binding.root)
    }

    container.onBind()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  private fun bindVisual() {
    binding.setVariable(visualVar, visual)
  }
}
