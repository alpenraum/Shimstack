package com.alpenraum.shimstack.usecases

sealed class Result {
    open class Success : Result()

    abstract class Failure : Result()

    fun isSuccess() = this is Success
}