package io.krugosvet.dailydish.common.repository

import io.krugosvet.dailydish.common.dto.NewImage
import java.io.File
import java.util.*

private const val IMAGE_DIR = "/static/"
private const val RESOURCES_DIR = "src/main/resources/$IMAGE_DIR"

@Suppress("BlockingMethodInNonBlockingContext")
internal class ImageRepository(
  private val hostUrl: String
) {

  /**
   * @return remote path to [newImage]
   */
  fun save(newImage: NewImage): String {
    val file = "${UUID.randomUUID()}.${newImage.extension}"
      .toFile()
      .apply {
        createNewFile()
        outputStream().buffered().use { output ->
          output.write(newImage.data)
        }
      }

    return hostUrl + IMAGE_DIR + file.name
  }

  fun delete(image: String?) {
    image ?: return

    val file = image.split("/").last().toFile()

    file.delete()
  }

  private fun String.toFile() = File(RESOURCES_DIR + this)
}
