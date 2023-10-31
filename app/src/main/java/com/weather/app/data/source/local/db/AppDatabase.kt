package com.weather.app.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weather.app.data.source.local.dao.WeatherDao
import com.weather.app.data.source.local.entity.FavoriteWeatherEntity

@Database(
    entities = [FavoriteWeatherEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}