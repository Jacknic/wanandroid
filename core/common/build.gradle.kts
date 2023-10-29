plugins {
    id("com.android.library")
}

android {
    namespace = "com.jacknic.android.core.common"
}

dependencies {
    api(libs.timber)
    implementation(libs.logger) { exclude(group = "com.android.support") }
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.startup)
}