package io.krugosvet.dailydish.android.architecture.extension

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.KClass

inline val <reified T : Any> KClass<T>.sealedObjects: List<T>
    get() = sealedSubclasses.map { it.objectInstance!! }

@Suppress("ReplaceNegatedIsEmptyWithIsNotEmpty")
@OptIn(ExperimentalContracts::class)
fun IntArray?.isNotEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotEmpty != null)
    }

    return this != null && !this.isEmpty()
}
