package life.chenshi.keepaccounts.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

// false means every module can run as an app
const val moduleRunAsApp = false

/**
 * 给Library模块进行配置
 */
class LibConfigPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            // 集成模式下, common模块和library组件都只能是library, 否则其他模块没法依赖运行
            if (project.name.contains("common") || project.name.contains("library")) {
                applyPluginsBlock(false)
                applyAndroidBlock(false)
            } else {
                // 其他模块可以任意切换
                applyPluginsBlock(moduleRunAsApp)
                applyAndroidBlock(moduleRunAsApp)
            }
            applyDependenciesBlock()
        }
    }
}