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
}

rootProject.name = "Wanandroid"
include(":app")

/**
 * 引用子模块
 */
fun installSubmodule(dir: String) {
    val itemList = File(rootDir, dir).listFiles(FileFilter { it.isDirectory })
    itemList?.forEach {
        include(":$dir:${it.name}")
    }
}

installSubmodule("core")
installSubmodule("apps")
