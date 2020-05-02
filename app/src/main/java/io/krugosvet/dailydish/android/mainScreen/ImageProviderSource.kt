package io.krugosvet.dailydish.android.mainScreen

enum class ImageProviderSource(val providerName: String) {
  CAMERA("Take a picture"),
  GALLERY("Pick from a gallery")
}

fun getImageProviderNames() = ImageProviderSource.values().map { it.providerName }.toTypedArray()

enum class ImageProviderMainAction(val actionName: String) {
  UPDATE("Update picture"),
  REMOVE("Remove picture")
}

fun getImageProviderMainActionNames() =
  ImageProviderMainAction.values().map { it.actionName }.toTypedArray()
