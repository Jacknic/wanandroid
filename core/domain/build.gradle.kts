plugins {
    kotlin("jvm")
}

dependencies {
    implementation(projects.core.model)
}

// AGENT.md: domain 模块禁止 Android 依赖，使用专用 detekt 配置
tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
    config.setFrom(files("$rootDir/detekt.yml", "detekt-domain.yml"))
}