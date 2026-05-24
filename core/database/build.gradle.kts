plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt.android)
}

dependencies {
    implementation(projects.core.model)
    implementation(androidx.room.roomKtx)
    implementation(androidx.room.roomRuntime)
    kapt(androidx.room.roomCompiler)
}
