package io.krugosvet.dailydish.android.utils.intent

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.support.annotation.StringRes
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import io.krugosvet.dailydish.android.BuildConfig
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.mainScreen.ImageProviderMainAction
import io.krugosvet.dailydish.android.mainScreen.ImageProviderSource
import io.krugosvet.dailydish.android.mainScreen.getImageProviderMainActionNames
import io.krugosvet.dailydish.android.mainScreen.getImageProviderNames
import io.krugosvet.dailydish.android.utils.createImageFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ImageProvider @JvmOverloads constructor(
        private val onPhotoReceiveCallback: (file: File?) -> Unit = {},
        private val onPhotoDelete: () -> Unit,
        mainImageIsEmpty: Boolean, private val hostActivity: Activity) {

    private var lastReceivedImage: File? = null

    init {
        if (mainImageIsEmpty) {
            openImageProviderChooser()
        } else {
            AlertDialog.Builder(hostActivity).setItems(getImageProviderMainActionNames(), { dialog, which ->
                when (ImageProviderMainAction.values()[which]) {
                    ImageProviderMainAction.UPDATE -> openImageProviderChooser()
                    ImageProviderMainAction.REMOVE -> onPhotoDelete.invoke()
                }
                dialog.dismiss()
            }).setNegativeButton(getString(R.string.dialog_cancel_button), { dialog, _ -> dialog.dismiss() })
                    .create().show()
        }
    }

    fun onActivityResultGalleryPick(data: Intent?) {
        lastReceivedImage = createImageFile(hostActivity)
        val inputStream = hostActivity.contentResolver.openInputStream(data?.data)
        inputStream.use {
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            FileOutputStream(lastReceivedImage).use {
                it.write(buffer)
            }
        }

        if (data != null) passLastReceivedImage()
    }

    fun passLastReceivedImage() = onPhotoReceiveCallback.invoke(lastReceivedImage)

    private fun openImageProviderChooser() {
        AlertDialog.Builder(hostActivity).setItems(getImageProviderNames(), { dialog, which ->
            when (ImageProviderSource.values()[which]) {
                ImageProviderSource.CAMERA -> openCamera()
                ImageProviderSource.GALLERY -> openGallery()
            }
            dialog.dismiss()
        }).setNegativeButton(getString(R.string.dialog_cancel_button), { dialog, _ -> dialog.dismiss() })
                .create().show()
    }

    fun startCamera() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (pictureIntent.resolveActivity(hostActivity.packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile(hostActivity)
                lastReceivedImage = photoFile
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            if (photoFile != null) {
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(hostActivity,
                        BuildConfig.APPLICATION_ID + FILE_PROVIDER_LOCATION, photoFile))
                hostActivity.startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE)
            }
        }
    }

    private fun openGallery() {
        hostActivity.startActivityForResult(requestPickImageIntent(hostActivity), REQUEST_PICK_GALLERY_IMAGE)
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(hostActivity, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(hostActivity, arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSION_CAMERA)
        }
    }

    private fun getString(@StringRes resId: Int) = hostActivity.getString(resId)

    private fun requestPickImageIntent(context: Context?): Intent {
        val actionGetContent = Intent(Intent.ACTION_GET_CONTENT).setType(IMAGE_DATA_TYPE)
        val pickExternalImage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                .setType(IMAGE_DATA_TYPE)

        return Intent.createChooser(actionGetContent, context?.getString(R.string.intent_select_image_from_gallery))
                .putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickExternalImage))
    }
}
