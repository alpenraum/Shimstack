package com.alpenraum.shimstack.datastore

@Deprecated("use ShimstackDatastore instead")
interface KeyValueStore {
    fun getAll(): Map<String?, *>?

    fun put(
        key: String,
        value: Set<String?>?
    )

    fun put(
        key: String,
        value: String
    )

    fun put(
        key: String,
        value: Boolean
    )

    fun put(
        key: String,
        value: Int
    )

    fun put(
        key: String,
        value: Long
    )

    fun <T : Any> putObject(
        key: String?,
        value: T,
        clazz: Class<T>
    )

    fun put(
        key: String,
        value: Double
    )

    fun hasStored(key: String): Boolean

    fun delete(key: String)

    fun readStringSet(
        key: String,
        defaultValue: Set<String?>?
    ): Set<String?>?

    fun readString(
        key: String,
        defaultValue: String?
    ): String?

    fun readBoolean(
        key: String,
        defaultValue: Boolean
    ): Boolean

    fun readInt(
        key: String,
        defaultValue: Int
    ): Int

    fun readLong(
        key: String,
        defaultValue: Long
    ): Long

    fun readDouble(
        key: String,
        defaultValue: Double
    ): Double

    fun <T> readObject(
        key: String?,
        type: Class<T>
    ): T?
}