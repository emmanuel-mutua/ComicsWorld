package com.droidcon.comicsworld.di

import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.droidcon.comicsworld.data.ComicsRepository
import com.droidcon.comicsworld.data.ComicsRepositoryDelegate
import com.droidcon.comicsworld.data.pref.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideComicsRepository(): ComicsRepository = ComicsRepositoryDelegate()

    @Provides
    fun provideUserPreferencesRepo(datastore: DataStore<Preferences>): UserPreferencesRepository =
        UserPreferencesRepository(dataStore = datastore)
}