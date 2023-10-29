plugins {
    id("com.android.library")
}

android {
    namespace = "com.jacknic.android.core.database"
}

dependencies {
    implementation(libs.room.ktx)
}