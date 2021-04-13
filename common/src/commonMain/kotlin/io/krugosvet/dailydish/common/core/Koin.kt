package io.krugosvet.dailydish.common.core

import io.krugosvet.dailydish.common.dto.dtoModule
import io.krugosvet.dailydish.common.repository.db.dbModule
import io.krugosvet.dailydish.common.repository.network.networkModule
import io.krugosvet.dailydish.common.usecase.useCaseModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

public val commonModules: List<Module> by lazy {
  listOf(
    dbModule,
    networkModule,
    useCaseModule,
    dtoModule,
    platformModule
  )
}

public fun init(): KoinApplication = startKoin {
  modules(commonModules)
}

internal expect val platformModule: Module
