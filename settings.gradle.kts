@file:Suppress("UnstableApiUsage")

import java.io.FileFilter

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("androidx") {
            from("androidx.gradle:gradle-version-catalog:2025.02.00")
        }
    }
}

rootProject.name = "Wanandroid"
include(":app")

/**
 * 引用子模块
 */
fun installSubmodule(dir: String) {
    val itemList = File(rootDir, dir).listFiles(FileFilter {
        File(it, "build.gradle.kts").exists() or
                File(it, "build.gradle").exists()
    })
    itemList?.forEach {
        include(":$dir:${it.name}")
    }
}

installSubmodule("core")
installSubmodule("apps")
