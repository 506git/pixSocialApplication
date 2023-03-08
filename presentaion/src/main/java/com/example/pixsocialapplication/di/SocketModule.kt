package com.example.pixsocialapplication.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.data.db.UserDao
import com.example.data.preferences.DefaultPreferences
import com.example.data.repository.DatabaseRepositoryImpl
import com.example.data.socket.SocketManager
import com.example.domain.database_usecase.DatabaseUseCase
import com.example.domain.database_usecase.GetUserInfo
import com.example.domain.database_usecase.InsertUserInfo
import com.example.domain.preferences.Preferences
import com.example.domain.repository.DatabaseRepository
import com.example.domain.socket.AppSocket
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Manager
import io.socket.client.Socket
import java.net.URI
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SocketModule {
    @Provides
    @Singleton
    fun provideSocket(): Socket {
        val options = IO.Options.builder()
            .setRememberUpgrade(false)
            .setUpgrade(true)
            .build()
        options.apply {
            reconnection = true
            reconnectionDelay = 1000
            timeout = 10000
        }

        return IO.socket(SOCKET_BASE_URL, options)
    }

    @Provides
    @Singleton
    fun provideAppSocket(socket: Socket): AppSocket {
        return SocketManager(socket)
    }

    private const val SOCKET_BASE_URL = "http://limgs.iptime.org:8086"

}