plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt.android)
}

dependencies {
    implementation(projects.core.analytics)
    implementation(projects.core.domain)
    implementation(projects.core.common)
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.notification)

//    implementation(libs.androidx.core.ktx)
//    implementation(libs.kotlinx.coroutines.android)
//    implementation(libs.kotlinx.datetime)
//    implementation(libs.kotlinx.serialization.json)

    testImplementation(projects.core.datastoreTest)
    testImplementation(projects.core.testing)
}
