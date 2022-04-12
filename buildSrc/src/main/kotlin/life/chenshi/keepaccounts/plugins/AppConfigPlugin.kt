package life.chenshi.keepaccounts.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 给Application模块进行配置
 */
class AppConfigPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            applyPluginsBlock(true)
            applyAndroidBlock(true)
            applyDependenciesBlock()
        }
    }
}