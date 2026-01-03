plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.hilt.android)
    id("kotlin-parcelize")
    alias(libs.plugins.compose.compiler)
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
    @Suppress("UnstableApiUsage")
    composeOptions {
        kotlinCompilerExtensionVersion = androidx.composeCompiler.compiler.get().version
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(androidx.lifecycle.lifecycleLivedataKtx)

    implementation(androidx.composeUi.uiToolingPreview)
    implementation(androidx.hilt.hiltNavigationCompose)
    implementation(androidx.navigation.navigationCompose)
    implementation(androidx.paging.pagingCompose)
    implementation(androidx.lifecycle.lifecycleViewmodelCompose)
    implementation(androidx.composeMaterial3.material3)
    implementation(androidx.composeMaterial3Adaptive.adaptiveNavigation)
    implementation(androidx.composeMaterial3.material3AdaptiveNavigationSuite)
    implementation(androidx.composeMaterial.materialIconsExtended)

    implementation(libs.coil.kt.compose)
    implementation(libs.lottie.compose)

    debugImplementation(androidx.composeUi.uiTooling)
    debugImplementation(androidx.composeUi.uiTestManifest)

    androidTestImplementation(androidx.composeUi.uiTestJunit4)

    // implementation("br.com.devsrsouza.compose.icons:font-awesome:1.1.0")
}

