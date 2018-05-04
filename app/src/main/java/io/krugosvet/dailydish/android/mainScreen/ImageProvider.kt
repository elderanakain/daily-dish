package io.krugosvet.dailydish.android.mainScreen

enum class ImageProvider(val providerName: String) {
    CAMERA("Camera"), GALLERY("Gallery") }

fun getImageProviderNames() = ImageProvider.values().map { it.providerName }.toTypedArray()
