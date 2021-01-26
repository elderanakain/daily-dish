package io.krugosvet.dailydish.common.dto

import kotlinx.serialization.Serializable

@Serializable
public data class NewImage(
  val data: ByteArray,
  val extension: String
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    other as NewImage

    if (!data.contentEquals(other.data)) return false
    if (extension != other.extension) return false

    return true
  }

  override fun hashCode(): Int {
    var result = data.contentHashCode()
    result = 31 * result + extension.hashCode()
    return result
  }
}
