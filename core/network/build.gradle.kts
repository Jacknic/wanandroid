plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.hilt.android)
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