@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.library")
    alias(libs.plugins.hilt.android)
}

dependencies {
    implementation(project(":core:analytics"))
    implementation(project(":core:domain"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:notification"))

//    implementation(libs.androidx.core.ktx)
//    implementation(libs.kotlinx.coroutines.android)
//    implementation(libs.kotlinx.datetime)
//    implementation(libs.kotlinx.serialization.json)

    testImplementation(project(":core:datastore_test"))
    testImplementation(project(":core:testing"))
}
