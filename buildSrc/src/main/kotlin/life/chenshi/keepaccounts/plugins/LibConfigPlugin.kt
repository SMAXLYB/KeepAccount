 package life.chenshi.keepaccounts.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

// false means every module can run as an app
const val isIntegrationMode = true

/**
 * 给Library模块进行配置
 */
class LibConfigPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            applyPluginsBlock(!isIntegrationMode)
            applyAndroidBlock(!isIntegrationMode)
            applyDependenciesBlock()
        }
    }
}