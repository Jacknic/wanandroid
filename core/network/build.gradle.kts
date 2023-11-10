@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.library")
    alias(libs.plugins.hilt.android)
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
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
}