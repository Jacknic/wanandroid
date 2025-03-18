plugins {
    alias(libs.plugins.com.android.library)
}

dependencies {
    api(libs.timber)
    implementation(libs.logger) { exclude(group = "com.android.support") }
    implementation(libs.kotlinx.coroutines.android)
    implementation(androidx.startup.startupRuntime)
}