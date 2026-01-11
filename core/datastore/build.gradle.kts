plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt.android)
}

dependencies {
    api(projects.core.model)
    implementation(projects.core.common)
    implementation(androidx.datastore.datastore)
    implementation(androidx.datastore.datastorePreferences)

    testImplementation(projects.core.datastoreTest)
    testImplementation(libs.kotlinx.coroutines.test)
}