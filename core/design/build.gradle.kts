plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
}

android {
    buildFeatures {
        compose = true
    }
    @Suppress("UnstableApiUsage")
    composeOptions {
        kotlinCompilerExtensionVersion = androidx.composeCompiler.compiler.get().version
    }
}

dependencies {
    implementation(androidx.composeMaterial3.material3)
    implementation(androidx.composeUi.uiToolingPreview)

    debugImplementation(androidx.composeUi.uiTooling)
    debugImplementation(androidx.composeUi.uiTestManifest)
}