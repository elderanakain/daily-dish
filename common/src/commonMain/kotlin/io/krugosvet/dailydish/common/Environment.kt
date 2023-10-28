package io.krugosvet.dailydish.common

import io.krugosvet.dailydish.common.core.platformModule
import io.krugosvet.dailydish.common.dto.dtoModule
import io.krugosvet.dailydish.common.repository.MealRepository
import io.krugosvet.dailydish.common.repository.db.dbModule
import io.krugosvet.dailydish.common.repository.network.networkModule
import io.krugosvet.dailydish.common.usecase.AddMealUseCase
import io.krugosvet.dailydish.common.usecase.DeleteMealUseCase
import io.krugosvet.dailydish.common.usecase.SetCurrentTimeToCookedDateMealUseCase
import io.krugosvet.dailydish.common.usecase.useCaseModule
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.koinApplication

public interface Environment {

    public val mealRepository: MealRepository
    public val addMealUseCase: AddMealUseCase
    public val deleteMealUseCase: DeleteMealUseCase
    public val setCurrentTimeToMealUseCase: SetCurrentTimeToCookedDateMealUseCase

    public fun start()

    public fun stop()

    public companion object {

        public fun init(): Environment =
            EnvironmentImpl().also { it.start() }
    }
}

internal class EnvironmentImpl : Environment, KoinComponent {

    private var koinApp: KoinApplication? = null

    override val mealRepository: MealRepository by inject()
    override val addMealUseCase: AddMealUseCase by inject()
    override val deleteMealUseCase: DeleteMealUseCase by inject()
    override val setCurrentTimeToMealUseCase: SetCurrentTimeToCookedDateMealUseCase by inject()

    override fun start() {
        koinApp = koinApplication {
            modules(
                dbModule,
                networkModule,
                useCaseModule,
                dtoModule,
                platformModule,
            )
        }
    }

    override fun stop() {
        koinApp?.close()
        koinApp = null
    }

    override fun getKoin(): Koin =
        koinApp?.koin ?: error("Koin is not initialized")
}
