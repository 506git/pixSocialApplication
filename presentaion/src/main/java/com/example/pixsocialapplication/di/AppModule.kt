package com.example.pixsocialapplication.di


import android.content.Context
import com.example.data.repository.AppRepositoryImpl
import com.example.data.repository.dataSource.TestRemoteDataSource
import com.example.data.service.PushService
import com.example.domain.preferences.Preferences
import com.example.domain.repository.AppRepository
import com.example.domain.usecase.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

//    @Provides
//    @Singleton
//    fun provideSharedPreferences(
//        app: Application
//    ): SharedPreferences {
//        return app.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
//    }
//
//    @Provides
//    @Singleton
//    fun providePreferences(sharedPreferences: SharedPreferences): Preferences {
//        return DefaultPreferences(sharedPreferences)
//    }

//    @Provides
//    fun provideFirebaseAuthInstance() = FirebaseAuth.getInstance()
//
//    @Provides
//    fun provideFirebaseDatabaseInstance() = FirebaseDatabase.getInstance()


    //    @Provides
//    @Singleton
//    fun provideCache(application: Application): Cache {
//        return Cache(application.cacheDir, 10L * 1024 * 1024)
//    }
//
//    @Provides
//    @Singleton
//    fun provideOkHttpClient(cache: Cache): OkHttpClient {
//        return OkHttpClient.Builder().apply {
//            cache(cache)
//            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
//            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
//            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
//            retryOnConnectionFailure(true)
////            addInterceptor(HttpLoggingInterceptor().apply {
////                level = HttpLoggingInterceptor.Level.BODY
////            })
//        }.build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideRetrofit(client: OkHttpClient): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(client)
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideDeliveryService(retrofit: Retrofit): LawMakerService {
//        return retrofit.create(LawMakerService::class.java)
//    }
    @Provides
    @Singleton
    fun provideRepository(
        auth: FirebaseAuth,
        firebaseDatabase: FirebaseDatabase,
        firebaseStorage: FirebaseStorage,
        testRemoteSource: TestRemoteDataSource,
        @ApplicationContext appContext : Context,
        pushService: PushService
    ): AppRepository {
        return AppRepositoryImpl(auth, firebaseDatabase,firebaseStorage, testRemoteSource, appContext, pushService)
    }

    @Provides
    fun provideUseCases(
        repository: AppRepository,
        preferences: Preferences
    ): UseCase {
        return UseCase(
            saveIntroViewStatus = IntroViewStatus(preferences = preferences),
            loadIntroViewStatus = LoadIntroViewStatus(preferences = preferences),
            setStringPreferences = SetStringPreferences(preferences = preferences),
            getStringPreferences = GetStringPreferences(preferences = preferences),
            signIn = SignIn(repository),
            signUp = SignUp(repository),
            signInWithGoogleIdToken = SignInWithGoogleIdToken(repository),
            googleAutoLogIn = GoogleAutoLogIn(repository),
            googleSignIn = GoogleSignIn(repository),
            updateUserProfile = UpdateUserProfile(repository),
            getTestData = GetTestDataUseCase(repository),
            initUserProfileInfo = InitUserProfileInfo(repository),
            getRoomInfo = GetRoomInfoUseCase(repository),
            findUserId = FindUserId(repository),
            getRoomChat = GetRoomChat(repository),
            sendChat = SendChat(repository),
            updateUserFcmToken = UpdateUserFcmToken(repository),
            getGalleryList = GetGalleryList(repository),
            sendImage = SendImage(repository)
        )
    }

//    companion object {
//        private const val BASE_URL = "https://picsum.photos/"
//        private const val READ_TIMEOUT = 15L
//        private const val WRITE_TIMEOUT = 15L
//        private const val CONNECT_TIMEOUT = 15L
//    }
}