package io.krugosvet.dailydish.android.mainScreen

import android.app.*
import android.net.*
import android.os.*
import android.view.*
import android.view.ViewGroup.LayoutParams.*
import android.widget.*
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.db.objects.meal.*
import io.krugosvet.dailydish.android.utils.*
import io.krugosvet.dailydish.android.utils.baseUi.*
import io.krugosvet.dailydish.android.utils.image.*
import io.krugosvet.dailydish.android.utils.intent.*
import io.realm.*
import io.realm.kotlin.*
import kotlinx.android.synthetic.main.dialog_add_meal.*
import java.util.*

class DialogAddMeal : BaseDialogFragment(), DatePickerDialog.OnDateSetListener {

  interface DialogAddMealListener {
    fun onAddButtonClick(mealTitle: String, mealDescription: String, parseDate: Date,
      mainImage: Uri)
  }

  private val forms = mutableListOf<BaseTextInputLayout>()
  private var mainImage: Uri = Uri.parse("")
  private var date: Date = getCurrentDate()

  private var meal = Meal().apply { id = -1 }

  private lateinit var cameraImagePipe: CameraImagePipe
  private lateinit var realm: Realm

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View =
    inflater.inflate(R.layout.dialog_add_meal, container)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    cameraImagePipe = activity as CameraImagePipe
    realm = (activity as BaseActivity).realm
    showKeyboard(title.editText!!)
    handleForms()
    loadMealMainImage(addMealImage, mainImage.path)

    realm.executeTransaction {
      it.insertOrUpdate(meal)
    }
    meal = realm.where(Meal::class.java).equalTo("id", -1 as Int).findFirst()!!
    meal.addChangeListener(
      RealmChangeListener {
        if (isAdded) {
          mainImage = Uri.parse(it.mainImage)
          loadMealMainImage(addMealImage, meal.mainImage)
        }
      }
    )

    addMealButton.setOnClickListener {
      if (areFormsValid()) {
        (activity as DialogAddMealListener).onAddButtonClick(title.getEditTextInput(),
          description.getEditTextInput(), date, mainImage)
        dismiss()
      }
    }

    addMealImage.setOnClickListener {
      cameraImagePipe.openMealMainImageUpdateDialog({ image ->
        meal.changeMainImage(realm, image)
      }, {
        mainImage = Uri.parse("")
        loadMealMainImage(addMealImage, mainImage.toString())
      }, mainImage.toString().isEmpty())
    }

    createDateForm()
  }

  override fun onResume() {
    super.onResume()
    dialog.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    outState?.putString("title", title.getEditTextInput())
    outState?.putString("description", description.getEditTextInput())
    outState?.putSerializable("date", date)
    super.onSaveInstanceState(outState)
  }

  override fun onViewStateRestored(savedInstanceState: Bundle?) {
    super.onViewStateRestored(savedInstanceState)
    if (savedInstanceState != null) {
      if (!savedInstanceState.getString("title").isNullOrEmpty()) title.editText?.setText(savedInstanceState.getString("title"))
      if (!savedInstanceState.getString("description").isNullOrEmpty()) description.editText?.setText(savedInstanceState.getString("description"))
      if (!savedInstanceState.getString("date").isNullOrEmpty()) dateEditText.editText?.setText(getLongFormattedDate(savedInstanceState.getSerializable("date") as Date))
    }
  }

  private fun handleForms() {
    forms.addAll(listOf(title, description))
    forms.forEach {
      it.addTextChangedListener(object : SimpleTextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
          if (s.isNullOrBlank()) it.error = getString(R.string.dialog_add_meal_empty_form_error, it.tag)
          else it.isErrorEnabled = false
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
    dateEditText.editText?.setText(getLongFormattedDate(date))
    dateEditText.editText?.setOnClickListener {

    }
  }

  override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
    date = defaultFormatDate(year, month + 1, dayOfMonth)
    dateEditText.editText?.setText(getLongFormattedDate(date))
  }
}
