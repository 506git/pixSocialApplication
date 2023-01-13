plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
}
android {
    compileSdk = Apps.compileSdk

    defaultConfig {
        minSdk = Apps.minSdk
    }


}
subprojects {
    apply{
        this.apply {"$rootDir/common.gradle.kts"}
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(AndroidX.APPCOMPAT)
    implementation(Kotlin.KOTLIN_STDLIB)
    testImplementation(TestLibs.JUNIT)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")

    // Retrofit2
    implementation(Libs.RETROFIT)

    // Gson 변환기
    implementation(Libs.GSON)

    // Google
    implementation(platform(Firebase.BOM))
    implementation(Firebase.ANALYTICS)
    implementation(Firebase.AUTH_UI)
    implementation(Firebase.AUTH)
    implementation(Google.GMS)

    // Hilt
    implementation(Libs.HILT)
    kapt(Libs.HILT_COMPILE)

    // Timber
    implementation(Libs.TIMBER)

    // Firebase
    implementation(Firebase.DATABASE)
    implementation(Firebase.STORAGE)

    //paging
    implementation(AndroidX.TEST_PAGING)

    //room
    implementation(Libs.ROOM)
    implementation(Libs.ROOM_COMPILE)

}