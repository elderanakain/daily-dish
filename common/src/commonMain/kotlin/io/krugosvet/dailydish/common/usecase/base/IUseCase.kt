package io.krugosvet.dailydish.common.usecase.base

public interface IUseCase<I, O> {

    public suspend fun execute(input: I): O
}
