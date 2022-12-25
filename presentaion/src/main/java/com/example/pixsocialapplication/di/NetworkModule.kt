package com.example.pixsocialapplication.di

import android.app.Application
import com.example.data.service.LawMakerService
import com.example.data.service.PushService
import com.example.data.service.TestService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class pushServer

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class baseServer


    @Provides
    @Singleton
    fun provideCache(application: Application): Cache {
        return Cache(application.cacheDir, 10L * 1024 * 1024)
    }

    @Provides
    @Singleton
    fun provideHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder().apply {
            cache(cache)
            connectTimeout(15L, TimeUnit.SECONDS)
            writeTimeout(15L, TimeUnit.SECONDS)
            readTimeout(15L, TimeUnit.SECONDS)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }.build()
    }

    @Provides
    @Singleton
    @baseServer
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @pushServer
    fun provideRetrofitPushInstance(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(PUSH_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideDeliveryService(@baseServer retrofit: Retrofit): LawMakerService {
        return retrofit.create(LawMakerService::class.java)
    }

    @Provides
    @Singleton
    fun provideDeliveryRemoteService(@baseServer retrofit: Retrofit): TestService {
        return retrofit.create(TestService::class.java)
    }


    @Provides
    @Singleton
    fun providePushService(@pushServer retrofit: Retrofit): PushService {
        return retrofit.create(PushService::class.java)
    }

//    private const val BASE_URL = "http://ec2-34-218-220-255.us-west-2.compute.amazonaws.com:3000"
    private const val BASE_URL = "http://m.sblib.seoul.kr:8888/SungBukWebService/"
    private const val PUSH_BASE_URL = "http://129.154.203.45:7777"

}