package io.krugosvet.dailydish.android.service.injection

import androidx.preference.PreferenceManager
import io.krugosvet.dailydish.android.service.DialogService
import io.krugosvet.dailydish.android.service.ImagePickerService
import io.krugosvet.dailydish.android.service.PreferenceService
import io.krugosvet.dailydish.android.ui.container.view.ContainerActivity
import org.koin.dsl.module

val serviceModule = module {

  scope<ContainerActivity> {

    scoped {
      ImagePickerService(get())
    }

    scoped {
      DialogService(get(), get())
    }
  }

  single {
    PreferenceService(
      PreferenceManager.getDefaultSharedPreferences(get()),
      get()
    )
  }
}
