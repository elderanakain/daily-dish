package io.krugosvet.dailydish.android.mainScreen

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.db.objects.Meal
import io.krugosvet.dailydish.android.utils.*
import io.realm.OrderedRealmCollection
import kotlinx.android.synthetic.main.fragment_meal_list.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*




const val NO_LIMIT = -1

open class MealListPageFragment : RealmFragment(), ViewPagerFragment, CameraImagePipe {

    protected val PAGE_TITLE = "pageTitle"

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

    var callback: (str: String) -> Unit = {}

    override fun openCamera(callback: (str: String) -> Unit) {
        this.callback = callback
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCameraActivity()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSION_CAMERA)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraActivity()
            }
        }
    }

    private fun startCameraActivity() {
        if (context != null) {
            val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (pictureIntent.resolveActivity(activity?.packageManager) != null) {
                // Create the File where the photo should go
                var photoFile: File? = null
                try {
                    photoFile = createImageFile();
                } catch (ex: IOException) {
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    val photoURI: Uri = FileProvider.getUriForFile(context!!,
                            "io.krugosvet.dailydish.fileprovider",
                            photoFile)
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            callback.invoke(mCurrentPhotoPath)
        }
    }

    var mCurrentPhotoPath: String = ""

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }
}

