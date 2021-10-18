package life.chenshi.keepaccounts.plugins

import Libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * 依赖配置
 */
internal fun Project.applyDependenciesBlock() {
    println("--->  ${project.name}配置dependencies ")
    dependencies {
        add("implementation", Libs.arouter)
        add("kapt", Libs.arouter_compiler)
    }
}