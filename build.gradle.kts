@file:Suppress("UNUSED_EXPRESSION")

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.android.sdklib.AndroidVersion.VersionCodes
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

buildscript {
    dependencies {
        classpath(libs.androidx.navigation.plugin)
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
}
true

fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
    add("implementation", dependencyNotation)

fun DependencyHandler.androidTestImplementation(dependencyNotation: Any): Dependency? =
    add("androidTestImplementation", dependencyNotation)

fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? =
    add("testImplementation", dependencyNotation)

fun DependencyHandler.debugImplementation(dependencyNotation: Any): Dependency? =
    add("debugImplementation", dependencyNotation)

fun DependencyHandler.kapt(dependencyNotation: Any): Dependency? =
    add("kapt", dependencyNotation)

/**
 * Android 模块统一配置
 */
fun CommonExtension<*, *, *, *, *>.configCommon(target: Project) {
    with(target) {
        version = libs.versions.module.get()
        pluginManager.apply("org.jetbrains.kotlin.android")
        pluginManager.apply("kotlin-kapt")
        pluginManager.apply("com.google.dagger.hilt.android")
        dependencies {
            implementation(libs.hilt.android)
            kapt(libs.hilt.android.compiler)

            androidTestImplementation(kotlin("test"))
            testImplementation(kotlin("test"))
            testImplementation(libs.junit)
        }
        if (parent?.name == "feature") {
            dependencies {
                implementation(project(":core:model"))
                implementation(project(":core:ui"))
                implementation(project(":core:design"))
                implementation(project(":core:data"))
                implementation(project(":core:common"))
                implementation(project(":core:domain"))
                implementation(project(":core:analytics"))

                testImplementation(kotlin("test"))
                testImplementation(project(":core:testing"))
                androidTestImplementation(kotlin("test"))
                androidTestImplementation(project(":core:testing"))
            }
        }
        (this as ExtensionAware).extensions.configure<KaptExtension>("kapt") {
            correctErrorTypes = true
        }
        (this as ExtensionAware).extensions.configure<KotlinAndroidProjectExtension>("kotlin") {
            jvmToolchain(8)
        }
    }


    compileSdk = VersionCodes.TIRAMISU
    defaultConfig {
        minSdk = VersionCodes.LOLLIPOP
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

/**
 * 应用模块统一配置
 */
fun BaseAppModuleExtension.configApplication(target: Project) {
    configCommon(target)
    with(target) {
        dependencies {
            implementation(libs.core.ktx)
            implementation(libs.lifecycle.runtime.ktx)
            implementation(libs.activity.compose)
            implementation(platform(libs.compose.bom))
            implementation(libs.ui)
            implementation(libs.ui.graphics)
            implementation(libs.ui.tooling.preview)
            implementation(libs.material3)

            androidTestImplementation(libs.androidx.test.ext.junit)
            androidTestImplementation(libs.espresso.core)
            androidTestImplementation(platform(libs.compose.bom))
            androidTestImplementation(libs.ui.test.junit4)

            debugImplementation(libs.ui.tooling)
            debugImplementation(libs.ui.test.manifest)
        }
    }
    compileSdk = VersionCodes.TIRAMISU
    defaultConfig {
        minSdk = VersionCodes.LOLLIPOP
        targetSdk = VersionCodes.TIRAMISU
        versionCode = 1
        versionName = "1.0"
        signingConfig = signingConfigs["debug"]
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs["debug"]
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }


    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

/**
 * 库模块统一配置
 */
fun LibraryExtension.configLibrary(target: Project) {
    configCommon(target)
    target.pluginManager.apply("maven-publish")
}

subprojects {
    val target = this

    pluginManager.withPlugin("com.android.application") {
        // println("${this.name} 插件已使用")
        extensions.configure<BaseAppModuleExtension>("android") { configApplication(target) }
    }
    pluginManager.withPlugin("com.android.library") {
        // println("${this.name} 插件已使用")
        extensions.configure<LibraryExtension>("android") { configLibrary(target) }
    }
    pluginManager.withPlugin("kotlin") {
        // println("${this.name} 插件已使用")
        pluginManager.apply("maven-publish")
        configure<KotlinProjectExtension> {
            jvmToolchain {
                languageVersion.set(JavaLanguageVersion.of(8))
            }
        }
    }

    pluginManager.withPlugin("maven-publish") {
        version = libs.versions.module.get()
        afterEvaluate {
            configure<PublishingExtension> {
                repositories {
                    maven {
                        name = "GitHubPackages"
                        url = uri("https://maven.pkg.github.com/Jacknic/wanandroid")
                        credentials {
                            username =
                                findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                            password = findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
                        }
                    }
                }
                publications {
                    create<MavenPublication>("gpr") {
                        val groupIdBuilder = StringBuilder("com.jacknic.android.wanandroid")
                        if (parent != null && parent != rootProject) {
                            groupIdBuilder.append(".${parent!!.name}")
                        }
                        groupId = groupIdBuilder.toString()
                        version = target.version.toString()
                        var targetComponent: SoftwareComponent? = components.findByName("release")
                        var packType = "aar"
                        if (targetComponent == null) {
                            targetComponent = components["java"]
                            packType = "jar"
                        }
                        from(targetComponent!!)
                        val artifactTask = tasks.run {
                            findByName("releaseSourcesJar") ?: getByName("kotlinSourcesJar")
                        }
                        artifact(artifactTask)
                        pom {
                            version = version
                            description.set(target.description)
                            packaging = packType
                            url.set("https://github.com/Jacknic/wanandroid")
                        }
                    }
                }
            }
        }
    }
}