plugins {
    id("com.android.library")
}

android {
    namespace = "com.jacknic.android.core.data"
}

dependencies {
    api(project(":core:analytics"))
    api(project(":core:domain"))
    api(project(":core:common"))
    api(project(":core:database"))
    api(project(":core:datastore"))
    api(project(":core:model"))
    api(project(":core:network"))
    api(project(":core:notification"))

//    implementation(libs.androidx.core.ktx)
//    implementation(libs.kotlinx.coroutines.android)
//    implementation(libs.kotlinx.datetime)
//    implementation(libs.kotlinx.serialization.json)

    testImplementation(project(":core:datastore-test"))
    testImplementation(project(":core:testing"))
}
