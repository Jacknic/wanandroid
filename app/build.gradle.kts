@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.com.android.application)
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.jacknic.android.wanandroid"
    defaultConfig {
        applicationId = "com.jacknic.android.wanandroid"
        versionCode = 1
        versionName = "1.0.0"
    }
}

dependencies {
    api(project(":core:data"))
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation("androidx.paging:paging-compose:1.0.0-alpha18")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
    implementation("androidx.compose.runtime:runtime-livedata")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    // implementation("br.com.devsrsouza.compose.icons:font-awesome:1.1.0")
}

