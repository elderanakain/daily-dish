package io.krugosvet.dailydish.android.mainScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.bumptech.glide.Glide
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.utils.*
import io.krugosvet.dailydish.android.utils.baseUi.BaseDialogFragment
import io.krugosvet.dailydish.android.utils.baseUi.BaseTextInputLayout
import io.krugosvet.dailydish.android.utils.image.withNoCache
import io.krugosvet.dailydish.android.utils.intent.CameraImagePipe
import kotlinx.android.synthetic.main.dialog_add_meal.*
import java.io.File
import java.util.*

class DialogAddMeal : BaseDialogFragment(), DatePickerDialog.OnDateSetListener {

    interface DialogAddMealListener {
        fun onAddButtonClick(mealTitle: String, mealDescription: String, parseDate: Date, mainImage: File?)
    }

    private val forms = mutableListOf<BaseTextInputLayout>()
    private var mainImage: File? = null

    lateinit var cameraImagePipe: CameraImagePipe

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = inflater.inflate(R.layout.dialog_add_meal, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showKeyboard(title.editText!!)
        handleForms()

        addMealButton.setOnClickListener {
            if (areFormsValid()) {
                (activity as DialogAddMealListener).onAddButtonClick(title.getEditTextInput(),
                        description.getEditTextInput(), parseDate(date.getEditTextInput()), mainImage)
                dismiss()
            }
        }

        addMealImage.setOnClickListener {
            cameraImagePipe.openMealMainImageUpdateDialog({ file ->
                mainImage = file
                Glide.with(activity).applyDefaultRequestOptions(
                        withNoCache().centerInside())
                        .load(file).into(addMealImage)
            }, {
                mainImage = null
                Glide.with(activity).applyDefaultRequestOptions(
                        withNoCache().centerInside())
                        .load(R.drawable.ic_insert_photo_black_48dp).into(addMealImage)
            }, mainImage == null)
        }

        createDateForm()
    }

    override fun onResume() {
        super.onResume()
        dialog.window.setLayout(getDimension(R.dimen.dialog_add_meal_width).toInt(), WRAP_CONTENT)
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        date.editText?.setText(getFormattedDate(year, monthOfYear, dayOfMonth))
    }

    fun addCameraImagePipe(cameraImagePipe: CameraImagePipe): DialogAddMeal {
        this.cameraImagePipe = cameraImagePipe
        return this
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
        date.editText?.setText(getCurrentDate())
        date.editText?.setOnClickListener {
            DatePickerDialog.newInstance(this, Calendar.getInstance()).apply {
                maxDate = Calendar.getInstance()
            }.show(fragmentManager, "")
        }
    }
}
