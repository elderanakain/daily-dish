package io.krugosvet.bindingcomponent

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*

interface IBindingContainer<TVisual> : LifecycleOwner {

  val bindingComponent: IBindingComponent<TVisual>

  val visual: MutableLiveData<TVisual>
    get() = bindingComponent.visual

  fun onBind() = Unit

  val parentContext: Context
}

/**
 * Represents binding part of UI layer
 */
interface IBindingComponent<TVisual> {

  @get:LayoutRes
  val layoutId: Int

  /**
   * Required visual model for data binding
   */
  val visual: MutableLiveData<TVisual>

  val rootView: View
}

class BindingComponent<TVisual>(
  @LayoutRes
  override val layoutId: Int,
  val container: IBindingContainer<TVisual>,
  val visualVar: Int
) :
  IBindingComponent<TVisual>,
  LifecycleObserver {

  override val visual = MutableLiveData<TVisual>()

  override val rootView: View
    get() = binding.root

  private lateinit var binding: ViewDataBinding

  init {
    container.lifecycle.addObserver(this)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  fun createBinding() {
    val context = container.parentContext

    binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, null, false)
    binding.lifecycleOwner = container

    if (container is AppCompatActivity) {
      container.setContentView(binding.root)
    }

    container.onBind()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  fun bindVisual() {
    binding.setVariable(visualVar, visual)
  }
}

