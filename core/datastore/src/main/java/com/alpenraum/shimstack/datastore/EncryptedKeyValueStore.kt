package com.alpenraum.shimstack.datastore

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.alpenraum.shimstack.datastore.DatastoreConstants.ANDROID_KEYSTORE
import com.alpenraum.shimstack.datastore.DatastoreConstants.ENCRYPTED_PREFERENCES_NAME
import com.squareup.moshi.Moshi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptedKeyValueStore
    @Inject
    constructor(
        private val context: Context,
        private val moshi: Moshi
    ) : KeyValueStore {
        private val encryptedSharedPreferences by lazy {
            val keyGenParameterSpec =
                KeyGenParameterSpec.Builder(
                    ANDROID_KEYSTORE,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
                    .apply {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            setUnlockedDeviceRequired(true)
                        }
                    }
                    .build()

            val masterKey =
                MasterKey.Builder(context, ANDROID_KEYSTORE)
                    .setKeyGenParameterSpec(keyGenParameterSpec)
                    .build()

            EncryptedSharedPreferences.create(
                context.applicationContext,
                ENCRYPTED_PREFERENCES_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

        private fun editor() = encryptedSharedPreferences.edit()

        override fun getAll(): Map<String?, Any> {
            return buildMap {
            }
        }

        override fun put(
            key: String,
            value: Set<String?>?
        ) {
            editor().putStringSet(key, value).apply()
        }

        override fun put(
            key: String,
            value: String
        ) {
            editor().putString(key, value).apply()
        }

        override fun put(
            key: String,
            value: Boolean
        ) {
            editor().putBoolean(key, value).apply()
        }

        override fun put(
            key: String,
            value: Int
        ) {
            editor().putInt(key, value).apply()
        }

        override fun put(
            key: String,
            value: Long
        ) {
            editor().putLong(key, value).apply()
        }

        override fun put(
            key: String,
            value: Double
        ) {
            editor().putLong(key, java.lang.Double.doubleToRawLongBits(value)).apply()
        }

        override fun <T : Any> putObject(
            key: String?,
            value: T,
            clazz: Class<T>
        ) {
            editor().putString(key, moshi.adapter(clazz).toJson(value)).apply()
        }

        override fun hasStored(key: String): Boolean {
            return encryptedSharedPreferences.contains(key)
        }

        override fun delete(key: String) {
            editor().remove(key).apply()
        }

        override fun readStringSet(
            key: String,
            defaultValue: Set<String?>?
        ): Set<String?>? {
            return encryptedSharedPreferences.getStringSet(key, defaultValue)
        }

        override fun readString(
            key: String,
            defaultValue: String?
        ): String? {
            return encryptedSharedPreferences.getString(key, defaultValue)
        }

        override fun readBoolean(
            key: String,
            defaultValue: Boolean
        ): Boolean {
            return encryptedSharedPreferences.getBoolean(key, defaultValue)
        }

        override fun readInt(
            key: String,
            defaultValue: Int
        ): Int {
            return encryptedSharedPreferences.getInt(key, defaultValue)
        }

        override fun readLong(
            key: String,
            defaultValue: Long
        ): Long {
            return encryptedSharedPreferences.getLong(key, defaultValue)
        }

        override fun readDouble(
            key: String,
            defaultValue: Double
        ): Double {
            val x =
                encryptedSharedPreferences.getLong(
                    key,
                    java.lang.Double.doubleToRawLongBits(defaultValue)
                )
            return java.lang.Double.longBitsToDouble(x)
        }

        override fun <T> readObject(
            key: String?,
            type: Class<T>
        ): T? {
            val x = encryptedSharedPreferences.getString(key, null)
            return x?.let { moshi.adapter<T>(type).fromJson(it) }
        }
    }