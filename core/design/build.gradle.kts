plugins {
    alias(libs.plugins.com.android.library)
}

android {
    buildFeatures {
        compose = true
    }
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