package io.krugosvet.dailydish.core.injection

import io.krugosvet.dailydish.core.service.DateService
import io.krugosvet.dailydish.core.service.IdGenerator
import io.krugosvet.dailydish.core.service.ResourceService
import org.koin.dsl.module

val coreModule = module {

  single {
    DateService()
  }

  single {
    IdGenerator()
  }

  single {
    ResourceService(get())
  }

}
