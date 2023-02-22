plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
}
android {
    compileSdk = 33

    defaultConfig {
        minSdk = Apps.minSdk
        targetSdk = 33
        multiDexEnabled = true
        versionCode = Apps.versionCode
        versionName = Apps.versionName
    }

    buildFeatures {
        viewBinding = true
    }
    androidResources {
        noCompress("json")
    }

    //------------------개인------
    signingConfigs {
        create("release") {
            keyAlias = "picSocial"
            keyPassword = "juns0305"
            storeFile = file("/Users/oyeongjun/AndroidStudioProjects/pixSocialApplication/Untitled")
            storePassword = "juns0305"
        }
    }
    //-------------------회사-------------
//    signingConfigs {
//        create("release") {
//            keyAlias = "picSocial"
//            keyPassword = "juns0305"
//            storeFile = file("C://Users/LG/keyStore/picsocial.jks")
//            storePassword = "juns0305"
//        }
//    }

    buildTypes{
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
        }
    }
    this.buildOutputs.all {
        val variantOutputImpl = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
        val variantName: String = variantOutputImpl.name
        val outputFileName = "pic_${defaultConfig.versionName}_${defaultConfig.versionCode}_${variantName}.apk"
        variantOutputImpl.outputFileName = outputFileName
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
    implementation(Libs.GLIDE_TRANS)
    implementation(Libs.GPU_FILTER)

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

    // lottie
    implementation(Libs.LOTTIE)

    //room
    implementation(Libs.ROOM)
    kapt(Libs.ROOM_COMPILE)
}


