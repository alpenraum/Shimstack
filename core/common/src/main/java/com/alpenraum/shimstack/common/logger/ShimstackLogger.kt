package com.alpenraum.shimstack.common.logger

interface ShimstackLogger {
    fun d(
        tag: String,
        msg: String
    )

    fun e(
        tag: String,
        msg: String,
        throwable: Throwable? = null
    )

    fun i(
        tag: String,
        msg: String
    )

    fun w(
        tag: String,
        msg: String
    )
}