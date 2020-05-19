package io.krugosvet.dailydish.android.architecture.extension

import kotlin.reflect.KClass

inline val <reified T: Any> KClass<T>.sealedObjects: List<T>
  get() = sealedSubclasses.map { it.objectInstance!! }
