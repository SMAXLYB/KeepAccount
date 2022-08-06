package life.chenshi.keepaccounts.plugins

import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

/**
 * 插件配置
 */
internal fun Project.applyPluginsBlock(runAsApp: Boolean) {
    println("--->  ${project.name}配置plugin ")

    this.apply(
        plugin = if (runAsApp) {
            "com.android.application"
        } else {
            "com.android.library"
        }
    )

    // kotlin插件
    this.apply(plugin = "kotlin-android") // 7.2版本必须在第一行， 否则报错找不到kotlin插件
    this.apply(plugin = "kotlin-kapt")

    if (project.name.contains("app") || project.name.contains("module")) {
        this.apply(plugin = "dagger.hilt.android.plugin")
    }
}