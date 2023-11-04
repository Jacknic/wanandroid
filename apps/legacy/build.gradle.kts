@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.com.android.application)
}

android {
    namespace = "com.jacknic.android.wanandroid"
    defaultConfig {
        applicationId = "com.jacknic.android.wanandroid"
        versionCode = 1
        versionName = "1.0.0"
    }

    buildFeatures {
        viewBinding {
            enable = true
        }
    }
}

dependencies {
    implementation(project(":core:data"))
    implementation(libs.hilt.android)
    kapt(libs.androidx.hilt.compiler)
}

