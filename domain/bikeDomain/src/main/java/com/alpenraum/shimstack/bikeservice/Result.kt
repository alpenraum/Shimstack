package com.alpenraum.shimstack.bikeservice

sealed class Result {
    open class Success : Result()

    abstract class Failure : Result()

    fun isSuccess() = this is Success
}