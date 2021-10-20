package life.chenshi.keepaccounts.plugins

import Libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

/**
 * 依赖配置
 */
internal fun Project.applyDependenciesBlock() {
    println("--->  ${project.name}配置dependencies ")
    dependencies {
        // 所有业务模块依赖common模块
        if ((project.name.contains("module") && !project.name.contains("common"))
            || project.name.contains("app")
        ) {
            println("--->  ${project.name}依赖了module_common")
            add("implementation", project(":module_common"))
        }

        add("implementation", Libs.arouter)
        add("kapt", Libs.arouter_compiler)
    }
}