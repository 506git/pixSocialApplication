package com.example.pixsocialapplication.di

import android.app.Application
import com.example.data.service.LawMakerService
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

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
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideDeliveryService(retrofit: Retrofit): LawMakerService {
        return retrofit.create(LawMakerService::class.java)
    }

    @Provides
    @Singleton
    fun provideDeliveryRemoteService(retrofit: Retrofit): TestService {
        return retrofit.create(TestService::class.java)
    }

//    private const val BASE_URL = "http://ec2-34-218-220-255.us-west-2.compute.amazonaws.com:3000"
    private const val BASE_URL = "http://m.sblib.seoul.kr:8888/SungBukWebService/"

}