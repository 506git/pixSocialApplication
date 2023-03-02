object Apps {
    const val compileSdk = 33
    const val minSdk = 21
    const val targetSdk = 33
    const val versionCode = 1
    const val versionName = "1.0.01"
}

object Versions {
    const val GRADLE = "4.2.1"

    // kotlin
    const val KOTLIN = "1.7.0"
    const val CORE_KTX = "1.7.0"

    // testLibs
    const val JUNIT = "4.13.2"

    // androidX
    const val APPCOMPAT = "1.4.2"
    const val CONSTRAINT = "2.1.4"
    const val VIEWPAGER = "1.0.0"
    const val NAVIGATION = "2.5.2"
    const val PAGING = "3.1.1"

    // Google
    const val MATERIAL = "1.7.0"
    const val FLEXBOX = "3.0.0"
    const val GMS = "20.2.0"

    // Firebase
    const val BOM = "30.3.1"
    const val AUTH_UI = "7.2.0"
//    const val ADMIN = "9.1.1"

    // Libs
    const val HILT_PLUG = "2.38.1"
    const val HILT = "2.40"

    // Timber
    const val TIMBER = "4.7.1"

    // Glide
    const val GLIDE = "4.11.0"
    const val GLIDE_TRANS = "4.3.0"

    const val RETROFIT = "2.9.0"
    const val OKHTTP = "4.3.1"
    const val LOTTIE = "5.2.0"

    // Room
    const val ROOM_VERSION = "2.5.0"

    // Socket
    const val SOCKET = "2.0.1"
}

object Kotlin {
    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.KOTLIN}"
    const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
}

object TestLibs {
    const val JUNIT = "junit:junit:${Versions.JUNIT}"
}

object AndroidX {
    const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val CONSTRAINT = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT}"
    const val VIEWPAGER = "androidx.viewpager2:viewpager2:${Versions.VIEWPAGER}"
    const val NAVIGATION = "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
    const val NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"
    const val PAGING3 = "androidx.paging:paging-runtime:${Versions.PAGING}"
    const val TEST_PAGING = "androidx.paging:paging-common:${Versions.PAGING}"
}

object Google {
    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
    const val FLEXBOX = "com.google.android.flexbox:flexbox:${Versions.FLEXBOX}"
    const val GMS = "com.google.android.gms:play-services-auth:${Versions.GMS}"
}

object Firebase {
    const val BOM = "com.google.firebase:firebase-bom:${Versions.BOM}"
    const val ANALYTICS = "com.google.firebase:firebase-analytics-ktx"
    const val AUTH_UI = "com.firebaseui:firebase-ui-auth:${Versions.AUTH_UI}"
    const val AUTH = "com.google.firebase:firebase-auth"
    const val DATABASE = "com.google.firebase:firebase-database"
    const val FCM = "com.google.firebase:firebase-messaging-ktx"
//    const val ADMIN = "com.google.firebase:firebase-admin:${Versions.ADMIN}"
    const val CLIENT = "com.google.api-client:google-api-client:1.32.2"
    const val STORAGE = "com.google.firebase:firebase-storage"
}

object Libs {
    const val HILT_PLUG = "com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT_PLUG}"
    const val HILT = "com.google.dagger:hilt-android:${Versions.HILT}"
    const val HILT_COMPILE =  "com.google.dagger:hilt-android-compiler:${Versions.HILT}"

    const val TIMBER = "com.jakewharton.timber:timber:${Versions.TIMBER}"

    const val GLIDE = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
    const val GLIDE_ANNOTATION = "com.github.bumptech.glide:compiler:${Versions.GLIDE}"
    const val GLIDE_TRANS = "jp.wasabeef:glide-transformations:${Versions.GLIDE_TRANS}"
    const val GPU_FILTER = "jp.co.cyberagent.android:gpuimage:2.1.0"

    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val GSON = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"

    const val OKHTTP                     = "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"
    const val OKHTTP_LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP}"

    const val LOTTIE = "com.airbnb.android:lottie:${Versions.LOTTIE}"

    const val ROOM = "androidx.room:room-runtime:${Versions.ROOM_VERSION}"
    const val ROOM_COMPILE = "androidx.room:room-compiler:${Versions.ROOM_VERSION}"

    const val SOCKET = "io.socket:socket.io-client:${Versions.SOCKET}"
}
