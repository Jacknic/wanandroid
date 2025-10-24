description = "模块依赖版本库"

plugins {
    `maven-publish`
    `version-catalog`
}

gradle.projectsEvaluated {
    catalog {
        versionCatalog {
            val parentProjects = rootProject.childProjects.filter {
                it.key in listOf("core", "data", "feature")
            }
            val groupId = libs.catalog.get().group
            parentProjects.forEach { p ->
                p.value.childProjects.forEach { (_, v) ->
                    if (v != project) {
                        val moduleName = "${p.value.name}-${v.name}"
                        val groupModule = "$groupId:$moduleName:${v.version}"
                        library(moduleName, groupModule)
                    }
                }
            }
        }
    }
}
