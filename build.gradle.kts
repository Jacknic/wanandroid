@file:Suppress("UnstableApiUsage")

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

val namespacePrefix = "com.jacknic.android.wanandroid"

// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
}

/**
 * 获取 Git 标签
 */
fun getGitTag(dir: String): String? {
    try {
        val undefined = "undefined"
        val process = Runtime.getRuntime().exec("git -C $dir name-rev --name-only --tags HEAD")
        process.waitFor()
        val tag = process.inputReader().readLine().trim()
        if (tag != undefined) {
            return tag
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

/**
 * Android 模块统一配置
 */
fun CommonExtension<*, *, *, *, *>.configCommon(target: Project) {
    with(target) {
        version = libs.versions.module.get()
        pluginManager.apply("org.jetbrains.kotlin.android")
        pluginManager.apply("kotlin-kapt")
    }

    compileSdk = VersionCodes.TIRAMISU
    defaultConfig {
        minSdk = VersionCodes.LOLLIPOP
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
    }

    (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    with(target) {
        dependencies {
            val commonPath = ":core:common"
            val commonProject = findProject(commonPath)
            if (target != commonProject) {
                "api"(project(commonPath))
            }

            "implementation"(libs.core.ktx)
            "androidTestImplementation"(kotlin("test"))
            "testImplementation"(kotlin("test"))
            "testImplementation"(libs.junit)
        }
        val isApp = pluginManager.hasPlugin("com.android.application")
        if (parent?.name == "feature" || isApp) {
            dependencies {
                "implementation"(project(":core:model"))
                "implementation"(project(":core:ui"))
                "implementation"(project(":core:design"))
                "implementation"(project(":core:data"))
                "implementation"(project(":core:domain"))
                "implementation"(project(":core:analytics"))

                "testImplementation"(kotlin("test"))
                "testImplementation"(project(":core:testing"))
                "androidTestImplementation"(kotlin("test"))
                "androidTestImplementation"(project(":core:testing"))
            }
        }

        testOptions {
            unitTests {
                isIncludeAndroidResources = true
            }
        }
        (this as ExtensionAware).extensions.configure<KaptExtension>("kapt") {
            correctErrorTypes = true
        }
        (this as ExtensionAware).extensions.configure<KotlinAndroidProjectExtension>("kotlin") {
            jvmToolchain(17)
        }
    }
}

/**
 * 应用模块统一配置
 */
fun BaseAppModuleExtension.configApplication(target: Project) {
    configCommon(target)
    with(target) {
        dependencies {
            "implementation"(libs.core.ktx)
            "implementation"(libs.lifecycle.runtime.ktx)
            "androidTestImplementation"(libs.androidx.test.ext.junit)
            "androidTestImplementation"(libs.espresso.core)
        }
    }
    compileSdk = VersionCodes.TIRAMISU

    signingConfigs {
        getByName("debug") {
            keyAlias = "debug"
            storePassword = "123456"
            keyPassword = "123456"
            storeFile = rootProject.file("debug.jks")
        }
    }

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
    val names = mutableListOf(namespacePrefix)
    val parent = target.parent
    if (parent != null && parent != rootProject) {
        names.add(parent.name)
    }
    names.add(target.name)
    namespace = names.joinToString(".")
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
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
    }

    pluginManager.withPlugin("com.google.dagger.hilt.android") {
        target.dependencies {
            "implementation"(libs.hilt.android)
            "kapt"(libs.hilt.android.compiler)
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
                            username = System.getenv("GITHUB_ACTOR")
                            password = System.getenv("GITHUB_TOKEN")
                        }
                    }
                }
                publications {
                    create<MavenPublication>("main") {
                        val groupIdBuilder = StringBuilder(namespacePrefix)
                        if (parent != null && parent != rootProject) {
                            groupIdBuilder.append(".${parent!!.name}")
                        }
                        val gitTag = getGitTag(target.projectDir.absolutePath)
                        groupId = groupIdBuilder.toString()
                        version = gitTag ?: "${target.version}-SNAPSHOT"
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