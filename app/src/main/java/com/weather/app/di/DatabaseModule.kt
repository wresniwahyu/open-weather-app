package com.weather.app.di

import android.content.Context
import androidx.room.Room
import com.weather.app.data.source.local.dao.WeatherDao
import com.weather.app.data.source.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    companion object {
        private const val APP_DB_NAME = "WeatherDB"
    }

    @Provides
    fun provideCurrenciesDao(
        appDatabase: AppDatabase
    ): WeatherDao {
        return appDatabase.weatherDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            APP_DB_NAME
        ).build()
    }

}