package life.chenshi.keepaccounts.plugins

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

/**
 * 插件配置
 */
internal fun Project.applyPluginsBlock(runAsApp: Boolean) {
    println("=========>>配置 ${project.name}")
    // kotlin插件
    this.apply(
        plugin = if (runAsApp) {
            "com.android.application"
        } else {
            "com.android.library"
        }
    )
    this.apply(plugin = "kotlin-kapt")
    this.apply(plugin = "kotlin-android")
}

/**
 * android块配置
 */
internal fun Project.applyAndroidBlock(runAsApp: Boolean) {
    val androidExt = if (runAsApp) {
        extensions.getByType<BaseAppModuleExtension>()
    } else {
        extensions.getByType<LibraryExtension>()
    }

    androidExt.apply {
        compileSdk = Sdk.compile_sdk_version

        defaultConfig {
            minSdk = Sdk.mini_sdk_version
        }
    }
}

/**
 * 依赖配置
 */
internal fun Project.applyDependenciesBlock() {

}