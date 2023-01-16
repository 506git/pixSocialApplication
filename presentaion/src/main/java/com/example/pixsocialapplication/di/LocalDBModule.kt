package com.example.pixsocialapplication.di

import android.content.Context
import androidx.room.Room
import com.example.data.db.UserDao
import com.example.data.db.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDBModule {
    @Provides
    @Singleton
    fun provideMovieDao(userDatabase: UserDatabase): UserDao {
        return userDatabase.userDao()
    }

    //room
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java, "localUser.db"
        ).allowMainThreadQueries().build()
    }
}