plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.jacknic.android.wanandroid"
    defaultConfig {
        applicationIdSuffix = ".legacy"
    }

    buildFeatures {
        viewBinding {
            enable = true
        }
    }
}

dependencies {
    implementation(projects.core.data)
}

