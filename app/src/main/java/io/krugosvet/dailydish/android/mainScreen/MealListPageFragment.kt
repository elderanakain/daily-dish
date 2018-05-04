package io.krugosvet.dailydish.android.mainScreen

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.db.objects.Meal
import io.krugosvet.dailydish.android.utils.*
import io.realm.OrderedRealmCollection
import kotlinx.android.synthetic.main.fragment_meal_list.*
import java.io.File
import java.io.FileOutputStream

const val NO_LIMIT = -1
const val PAGE_TITLE = "pageTitle"

open class MealListPageFragment : RealmFragment(), ViewPagerFragment, CameraImagePipe {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = inflater.inflate(R.layout.fragment_meal_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealList.adapter = MealListAdapter(getRealm(), getAdapterItems(), getAdapterItemLimit(), this)
    }

    override fun getFragmentTitle() = arguments?.getString(PAGE_TITLE) ?: ""

    protected open fun getAdapterItems(): OrderedRealmCollection<Meal> = getRealm().getMeals()

    protected open fun getAdapterItemLimit() = NO_LIMIT

    companion object {
        fun newInstance(pageTitle: String) = MealListPageFragment().apply {
            arguments = Bundle().apply { putString(PAGE_TITLE, pageTitle) }
        }
    }

    var callback: (file: File?) -> Unit = {}

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera(this, {mCurrentPhoto = it})
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ( resultCode == RESULT_OK) {
            when(requestCode) {
                REQUEST_CAPTURE_IMAGE -> callback.invoke(mCurrentPhoto)
                REQUEST_PICK_GALLERY_IMAGE -> {
                    val tempFile = createImageFile(context!!)
                    val inputStream = context!!.contentResolver.openInputStream(data?.data)
                    inputStream.use {
                        val buffer = ByteArray(inputStream.available())
                        inputStream.read(buffer)
                        FileOutputStream(tempFile).use {
                            it.write(buffer)
                        }
                    }

                    if(data != null) callback.invoke(tempFile)
                }
            }
        }
    }

    var mCurrentPhoto: File? = null

    override fun openImageProviderChooser(onPhotoReceiveCallback: (file: File?) -> Unit) {
        this.callback = onPhotoReceiveCallback
        AlertDialog.Builder(activity).setTitle(R.string.pick_image_provider)
                .setItems(getImageProviderNames(), { dialog, which ->
                    when(ImageProvider.values()[which]) {
                        ImageProvider.CAMERA -> openCamera(this, {mCurrentPhoto = it})
                        ImageProvider.GALLERY -> openGallery(this)
                    }
                    dialog.dismiss()
                }).create().show()
    }
}

