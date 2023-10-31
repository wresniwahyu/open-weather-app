package com.weather.app.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weather.app.data.source.local.entity.FavoriteWeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavoriteWeatherEntity)

    @Query("SELECT * FROM FavoriteWeatherEntity ORDER BY id ASC")
    fun getFavorites(): Flow<List<FavoriteWeatherEntity>>

    @Delete
    suspend fun delete(entity: FavoriteWeatherEntity)

}