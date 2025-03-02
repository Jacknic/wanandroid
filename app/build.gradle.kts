@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.hilt.android)
    alias(androidx.plugins.androidxNavigationSafeargsKotlinGradlePlugin)
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
        kotlinCompilerExtensionVersion = androidx.composeCompiler.compiler.get().version
    }
}

dependencies {
    implementation(project(":core:data"))
    implementation(androidx.hilt.hiltNavigationCompose)
    implementation(androidx.navigation.navigationCompose)
    implementation(androidx.paging.pagingCompose)
    implementation(androidx.lifecycle.lifecycleViewmodelCompose)
    implementation(androidx.lifecycle.lifecycleLivedataKtx)
    implementation(androidx.composeMaterial3.material3)
    implementation(libs.coil.kt.compose)
    implementation(libs.lottie.compose)

    androidTestImplementation(androidx.composeUi.uiToolingPreview)
    androidTestImplementation(androidx.composeUi.uiTestJunit4)

    debugImplementation(androidx.composeUi.uiTooling)
    debugImplementation(androidx.composeUi.uiTestManifest)

    // implementation("br.com.devsrsouza.compose.icons:font-awesome:1.1.0")
}

