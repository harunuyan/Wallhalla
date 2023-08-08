package com.volie.wallhalla.data.di

import android.content.Context
import androidx.room.Room
import com.volie.wallhalla.data.local.db.CuratedResponseDao
import com.volie.wallhalla.data.local.db.WallpaperDatabase
import com.volie.wallhalla.util.Constant.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideCuratedResponseDatabase(@ApplicationContext context: Context): WallpaperDatabase {
        return Room.databaseBuilder(
            context,
            WallpaperDatabase::class.java,
            DATABASE_NAME
        ).build(
        )
    }

    @Singleton
    @Provides
    fun providesCuratedResponseDao(database: WallpaperDatabase): CuratedResponseDao {
        return database.getCuratedResponseDao()
    }
}