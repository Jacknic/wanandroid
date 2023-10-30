plugins {
    id("com.android.library")
}

android {
    namespace = "com.jacknic.android.core.domain"
}

dependencies {
    implementation(project(":core:model"))
}