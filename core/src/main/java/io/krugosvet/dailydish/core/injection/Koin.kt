package io.krugosvet.dailydish.core.injection

import android.content.Context
import io.krugosvet.dailydish.core.service.DateService
import io.krugosvet.dailydish.core.service.IdGenerator
import io.krugosvet.dailydish.core.service.ResourceService
import org.koin.dsl.module

@Suppress("RemoveExplicitTypeArguments")
val coreModule = module {

  single<DateService> {
    DateService()
  }

  single<IdGenerator> {
    IdGenerator()
  }

  single<ResourceService> {
    ResourceService(
      get<Context>()
    )
  }

}
