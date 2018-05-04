package io.krugosvet.dailydish.android.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import io.krugosvet.dailydish.android.BuildConfig
import io.krugosvet.dailydish.android.R
import java.io.File
import java.io.IOException

//Request codes
const val REQUEST_CAPTURE_IMAGE = 1
const val REQUEST_PERMISSION_CAMERA = 2
const val REQUEST_PICK_GALLERY_IMAGE = 3

const val IMAGE_DATA_TYPE = "image/*"

private val actionGetContent = Intent(Intent.ACTION_GET_CONTENT).setType(IMAGE_DATA_TYPE)
private val pickExternalImage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        .setType(IMAGE_DATA_TYPE)

fun openGallery(activity: Activity) {
    activity.startActivityForResult(requestPickImageIntent(activity), REQUEST_PICK_GALLERY_IMAGE)
}

fun openGallery(fragment: Fragment) {
    fragment.startActivityForResult(requestPickImageIntent(fragment.context), REQUEST_PICK_GALLERY_IMAGE)
}

fun openCamera(fragment: Fragment, photoGetter: (File) -> Unit) {
    if (fragment.context != null) {
        if (ContextCompat.checkSelfPermission(fragment.context!!, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera(fragment, photoGetter)
        } else {
            fragment.requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSION_CAMERA)
        }
    }
}

fun startCamera(fragment: Fragment, photoGetter: (File) -> Unit) {
    val context = fragment.context!!
    val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (pictureIntent.resolveActivity(context.packageManager) != null) {
        var photoFile: File? = null
        try {
            photoFile = createImageFile(context)
            photoGetter.invoke(photoFile)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        if (photoFile != null) {
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".fileprovider", photoFile))
            fragment.startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE)
        }
    }
}

private fun requestPickImageIntent(context: Context?) = Intent
        .createChooser(actionGetContent, context?.getString(R.string.intent_select_image_from_gallery))
        .putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickExternalImage))
