package io.krugosvet.dailydish.common.core

import io.ktor.http.ContentType

public fun ContentType.Image.parse(key: String): ContentType =
  listOf(GIF, JPEG, PNG, SVG, XIcon)
    .firstOrNull { key.contains(it.contentSubtype) }
    ?: Any
