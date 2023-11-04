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

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
}

dependencies {
    implementation(project(":core:data"))
    implementation(platform(libs.compose.bom))
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.paging.compose)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
    implementation("androidx.compose.runtime:runtime-livedata")
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.material3)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)

    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // implementation("br.com.devsrsouza.compose.icons:font-awesome:1.1.0")
}

