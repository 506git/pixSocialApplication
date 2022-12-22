plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
}
android {
    compileSdk = 32

    defaultConfig {
        minSdk = Apps.minSdk
        targetSdk = 32
        multiDexEnabled = true
    }

    buildFeatures {
        viewBinding = true
    }
    androidResources {
        noCompress("json")
    }
}
subprojects {
    afterEvaluate{
        this.apply {"$rootDir/common.gradle.kts"}
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Kotlin.CORE_KTX)
    implementation(Kotlin.KOTLIN_STDLIB)
    implementation(AndroidX.APPCOMPAT)
    implementation(Google.MATERIAL)
    implementation(AndroidX.CONSTRAINT)
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")

    testImplementation(TestLibs.JUNIT)
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")

    //paging3
    implementation(AndroidX.PAGING3)
    testImplementation(AndroidX.TEST_PAGING)

    // Hilt
    implementation(Libs.HILT)
    kapt(Libs.HILT_COMPILE)

    // Timber
    implementation(Libs.TIMBER)

    // FlexBox Layout
    implementation (Google.FLEXBOX)

    // ViewPager2
    implementation(AndroidX.VIEWPAGER)

    // Glide
    implementation(Libs.GLIDE)
    annotationProcessor(Libs.GLIDE_ANNOTATION)

    // Navigation
    implementation(AndroidX.NAVIGATION)
    implementation(AndroidX.NAVIGATION_UI)

    // Retrofit2
    implementation(Libs.RETROFIT)
    
    // okhttp
    implementation(Libs.OKHTTP)
    implementation(Libs.OKHTTP_LOGGING_INTERCEPTOR)

    // Gson 변환기
    implementation(Libs.GSON)

    // Firebase
    implementation(platform(Firebase.BOM))
    implementation(Firebase.ANALYTICS)
    implementation(Firebase.AUTH_UI)
    implementation(Firebase.AUTH)
    implementation(Firebase.DATABASE)
    implementation(Firebase.FCM)
    implementation(Firebase.STORAGE)
//    implementation(Firebase.CLIENT)
    implementation(Google.GMS)


}
