package com.cabify.data.di

import android.content.Context
import androidx.room.Room
import com.cabify.common.Constants.DB_NAME
import com.cabify.data.db.CabifyDataBase
import com.cabify.data.db.IProductLocal
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideChannelDao(database: CabifyDataBase): IProductLocal {
        return database.iProductLocal()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): CabifyDataBase {
        return Room.databaseBuilder(
            appContext,
            CabifyDataBase::class.java,
            DB_NAME
        ).build()
    }
}
