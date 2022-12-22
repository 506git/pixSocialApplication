plugins {
    id("com.android.application") version "7.2.1" apply false
    id("com.android.library") version "7.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
//    id("org.jetbrains.kotlin.jvm") version "1.6.10" apply false
}

buildscript{
    dependencies {
        classpath("com.google.gms:google-services:4.3.13")
        classpath(Libs.HILT_PLUG)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}