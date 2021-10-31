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

        add("implementation", Libs.arouter)
        add("kapt", Libs.arouter_compiler)
        add("kapt", Libs.auto_service_annotations)

        // 项目依赖
        // common模块不依赖其他业务模块
        if (project.name.contains("common")) {
            parent?.subprojects?.filter { it.name.contains("library") }?.forEach {
                println("   --->  common依赖了${it.name}")
                add("api", project(":${it.name}"))
            }
            return@dependencies
        }

        // app壳依赖所有
        if (project.name.contains("app")) {
            // 集成模式下依赖所有, 独立模式下不能依赖module模块
            if (!moduleRunAsApp) {
                parent?.subprojects?.filter { !it.name.contains("app") }?.forEach {
                    println("   --->  app依赖了${it.name}")
                    add("implementation", project(":${it.name}"))
                }
            } else {
                parent?.subprojects?.filter { !it.name.contains("app") && !it.name.contains("module") }
                    ?.forEach {
                        println("   --->  app依赖了${it.name}")
                        add("implementation", project(":${it.name}"))
                    }
            }
        }

        // 所有业务模块依赖common模块
        if ((project.name.contains("module"))) {
            println("   --->  ${project.name}依赖了module_common")
            add("implementation", project(":module_common"))
        }
    }
}