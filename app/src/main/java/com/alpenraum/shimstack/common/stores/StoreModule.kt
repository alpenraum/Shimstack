package com.alpenraum.shimstack.common.stores

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class StoreModule {

    @Provides
    fun provideKeyValueStore(context: Context, moshi: Moshi): KeyValueStore =
        EncryptedKeyValueStore(
            context,
            moshi
        )
}
