plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
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
    implementation(AndroidX.APPCOMPAT)
    implementation(Kotlin.KOTLIN_STDLIB)

    testImplementation(TestLibs.JUNIT)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")

    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")

    //paging
    implementation(AndroidX.TEST_PAGING)
}