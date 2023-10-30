plugins {
    id("com.android.library")
}

android {
    namespace = "com.jacknic.android.core.network"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.svg)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.okhttp.logging)
    api(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
}