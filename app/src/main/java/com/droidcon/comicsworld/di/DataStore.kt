package com.droidcon.comicsworld.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private const val USER_PREFERENCES_NAME = "comics_world_user_preferences"

    @Provides
    @Singleton
    fun providePreferenceDataStore(@ApplicationContext applicationContext: Context) : DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile ={ applicationContext.preferencesDataStoreFile(USER_PREFERENCES_NAME)}
    )
}