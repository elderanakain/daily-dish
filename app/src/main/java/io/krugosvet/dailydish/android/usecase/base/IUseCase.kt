package io.krugosvet.dailydish.android.usecase.base

interface IUseCase<I, O> {

  suspend fun execute(input: I): Result<O>

}
