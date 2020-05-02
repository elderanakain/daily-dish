package io.krugosvet.dailydish.android.mainScreen

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.DatePicker
import io.krugosvet.bindingcomponent.BindingComponent
import io.krugosvet.bindingcomponent.IBindingComponent
import io.krugosvet.dailydish.android.BR
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.ui.BaseActivity
import io.krugosvet.dailydish.android.architecture.ui.BaseFragment
import io.krugosvet.dailydish.android.dagger.AppComponent
import io.krugosvet.dailydish.android.db.objects.meal.Meal
import io.krugosvet.dailydish.android.utils.SimpleTextWatcher
import io.krugosvet.dailydish.android.utils.baseUi.BaseTextInputLayout
import io.krugosvet.dailydish.android.utils.defaultFormatDate
import io.krugosvet.dailydish.android.utils.getCurrentDate
import io.krugosvet.dailydish.android.utils.intent.CameraImagePipe
import io.realm.RealmChangeListener
import io.realm.kotlin.addChangeListener
import java.util.*

class DialogAddMeal :
  BaseFragment<DialogAddMeal.Visual>(),
  DatePickerDialog.OnDateSetListener {

  interface DialogAddMealListener {

    fun onAddButtonClick(
      mealTitle: String, mealDescription: String, parseDate: Date, mainImage: Uri
    )

  }

  class Visual

  override val bindingComponent: IBindingComponent<Visual> =
    BindingComponent(layoutId = R.layout.dialog_add_meal, container = this, visualVar = BR.visual)

  override val parentContext: Context
    get() = requireContext()

  private val forms = mutableListOf<BaseTextInputLayout>()
  private var mainImage: Uri = Uri.parse("")
  private var date: Date = getCurrentDate()

  private var meal = Meal().apply { id = -1 }

  private lateinit var cameraImagePipe: CameraImagePipe

  override fun inject(appComponent: AppComponent) = Unit

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    cameraImagePipe = activity as CameraImagePipe
    realm = (activity as BaseActivity<*>).realm
    // showKeyboard(binding.title.editText!!)
    handleForms()
    // loadMealMainImage(binding.addMealImage, mainImage.path)

    realm.executeTransaction {
      it.insertOrUpdate(meal)
    }

    meal = realm.where(Meal::class.java).equalTo("id", -1 as Int).findFirst()!!
    meal.addChangeListener(
      RealmChangeListener {
        if (isAdded) {
          mainImage = Uri.parse(it.mainImage)
          // loadMealMainImage(binding.addMealImage, meal.mainImage)
        }
      }
    )

//    binding.addMealButton.setOnClickListener {
//      if (areFormsValid()) {
//        (activity as DialogAddMealListener).onAddButtonClick(binding.title.getEditTextInput(),
//          binding.description.getEditTextInput(), date, mainImage)
//        dismiss()
//      }
//    }

//    binding.addMealImage.setOnClickListener {
//      cameraImagePipe.openMealMainImageUpdateDialog(
//        { image -> meal.changeMainImage(realm, image) },
//        {
//          mainImage = Uri.parse("")
//          loadMealMainImage(binding.addMealImage, mainImage.toString())
//        },
//        mainImage.toString().isEmpty()
//      )
//    }

    createDateForm()
  }

  override fun onResume() {
    super.onResume()

    requireDialog().window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
  }

  private fun handleForms() {
    // forms.addAll(listOf(binding.title, binding.description))
    forms.forEach {
      it.addTextChangedListener(object : SimpleTextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
          if (s.isNullOrBlank()) {
            it.error = getString(R.string.dialog_add_meal_empty_form_error, it.tag)
          } else {
            it.isErrorEnabled = false
          }
        }
      })
    }
  }

  private fun areFormsValid(): Boolean {
    var isValid = true

    forms.forEach {
      it.triggerTextWatcher()
      if (it.isErrorEnabled && isValid) isValid = false
    }

    return isValid
  }

  private fun createDateForm() {
    // binding.dateEditText.editText?.setText(getLongFormattedDate(date))
  }

  override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
    date = defaultFormatDate(year, month + 1, dayOfMonth)

    // binding.dateEditText.editText?.setText(getLongFormattedDate(date))
  }
}
