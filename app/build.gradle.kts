@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.hilt.android)
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
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":core:data"))
    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.paging.compose)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation(libs.material3)
    implementation(libs.coil.kt.compose)
    api(libs.ui.tooling.preview)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)

    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // implementation("br.com.devsrsouza.compose.icons:font-awesome:1.1.0")
}

