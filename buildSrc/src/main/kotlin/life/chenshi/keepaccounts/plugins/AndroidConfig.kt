package life.chenshi.keepaccounts.plugins

import Sdk
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * android块配置
 */
internal fun Project.applyAndroidBlock(runAsApp: Boolean) {
    println("--->  ${this.name}配置android ")
    if (runAsApp) {
        extensions.getByType<BaseAppModuleExtension>().applyConfig(this)
    } else {
        extensions.getByType<LibraryExtension>().applyConfig(this)
    }

    extensions.configure<KaptExtension>("kapt") {
        arguments {
            arg("AROUTER_MODULE_NAME", project.name)
        }
    }

}

internal fun BaseAppModuleExtension.applyConfig(project: Project) {
    resourcePrefix = project.resourcePrefix

    compileSdk = Sdk.compile_sdk_version

    sourceSets {
        getByName("main") {
            // 如果是app工程, 直接使用main下的xml
            if (!project.name.contains("_")) {
                manifest.srcFile("src/main/AndroidManifest.xml")
            } else {
                val manifestFile =
                    File("${project.projectDir}/src/main/kotlin/run_as_app/AndroidManifest.xml")
                println("    --->  ${project.name} >> run_as_app/manifest.xml是否存在：${manifestFile.exists()}")
                // 如果是模块, 不能直接使用main
                if (manifestFile.exists()) {
                    manifest.srcFile("src/main/kotlin/run_as_app/AndroidManifest.xml")
                } else {
                    manifest.srcFile("src/main/AndroidManifest.xml")
                }
            }
        }
    }

    defaultConfig {
        applicationId = project.applicationId
        minSdk = Sdk.mini_sdk_version
        targetSdk = Sdk.target_sdk_version
        versionCode = Sdk.version_code
        versionName = Sdk.version_name
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // 声明多维度 对flavor进行分组, 从哪些维度区分apk, 可以从是否收费/上架市场/运行环境等等作为维度
        flavorDimensions.add("environment")
    }


    applicationVariants.all {
        val versionName = this.versionName
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                this.outputFileName =
                    "${this.outputFileName.substringBefore(".")}_${buildTime()}_${versionName}.apk"
            }
        }
    }

    // 签名配置
    signingConfigs {
        register("release") {
            storeFile = File("${project.rootDir}/key/keepaccount.jks")
            storePassword = "19980909a"
            keyAlias = "keepaccount"
            keyPassword = "19980909a"

            enableV1Signing = true
            enableV2Signing = true
        }
    }

    buildTypes {
        release {
            isDebuggable = true
            isMinifyEnabled = true
            // 自动清理未使用资源 开启minifyEnabled有效
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }

        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
        }
    }

    // 多渠道
    productFlavors {
        // 在环境的维度上创建了线上环境的风味, 接下来可以在代码中动态获取urL
        create("Product") {
            dimension = "environment"
            // manifest占位
            // manifestPlaceholders.put("CHANNEL","normal")
            // buildConfig动态配置
            // buildConfigField "String","INDEX_URL",""http://chenshi.life""
            // 资源动态配置
            resValue("string", "app_name", project.appName)
        }

        // 同理开发环境
        create("Dev") {
            dimension = "environment"
            // manifest占位
            // manifestPlaceholders.put("CHANNEL","normal")
            // buildConfig动态配置
            // buildConfigField "String","INDEX_URL",""http://chenshi.life""
            // 资源动态配置
            resValue("string", "app_name", "${project.appName}_开发版")
            // dev版本加个后缀，防止覆盖
            applicationId = "${project.applicationId}.dev"
        }
    }
    // manifest文件变量占位
    // productFlavors.all{ flavor ->
    //     manifestPlaceholders.put("CHANNEL","normal")
    // }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
        jvmTarget = "1.8"
    }
}

internal fun LibraryExtension.applyConfig(project: Project) {
    resourcePrefix = project.resourcePrefix

    compileSdk = Sdk.compile_sdk_version

    defaultConfig {
        minSdk = Sdk.mini_sdk_version
        targetSdk = Sdk.target_sdk_version
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        // 声明多维度 对flavor进行分组, 从哪些维度区分apk, 可以从是否收费/上架市场/运行环境等等作为维度
        flavorDimensions.add("environment")
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/main/AndroidManifest.xml")
            kotlin {
                /* 升级后exclude失效了, kotlin资源统一放在包名下*/
                srcDir("src/main/kotlin/life/chenshi/keepaccounts/")
                // exclude("src/main/kotlin/run_as_app/**")
            }
        }
    }

    libraryVariants.all {
        outputs.all {
            if (this is com.android.build.gradle.internal.api.LibraryVariantOutputImpl) {
                this.outputFileName =
                    "${this.outputFileName.substringBefore(".")}_${buildTime()}.aar"
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            // 自动清理未使用资源 开启minifyEnabled有效
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }

    // 多渠道
    productFlavors {
        // 在环境的维度上创建了线上环境的风味, 接下来可以在代码中动态获取urL
        create("Product") {
            dimension = "environment"
            // manifest占位
            // manifestPlaceholders.put("CHANNEL","normal")
            // buildConfig动态配置
            // buildConfigField "String","INDEX_URL",""http://chenshi.life""
            // 资源动态配置
            resValue("string", "app_name", project.appName)
        }

        // 同理测试环境
        create("Dev") {
            dimension = "environment"
            // manifest占位
            // manifestPlaceholders.put("CHANNEL","normal")
            // buildConfig动态配置
            // buildConfigField "String","INDEX_URL",""http://chenshi.life""
            // 资源动态配置
            resValue("string", "app_name", "${project.appName}_开发版")
        }
    }
    // manifest文件变量占位
    // productFlavors.all{ flavor ->
    //     manifestPlaceholders.put("CHANNEL","normal")
    // }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
        jvmTarget = "1.8"
    }
}

internal val Project.resourcePrefix: String
    get() {
        // 如果包含_, 直接取最后的单词拼接, 如果不包含, 直接拼接
        if (!this.name.contains("_")) {
            return "${this.name}_"
        }
        return name.substringAfter("_").plus("_")
    }

internal val Project.applicationId: String
    get() {
        val id = this.name.replace("_", ".")
        return "life.chenshi.keepaccounts.$id"
    }

internal val Project.appName: String
    get() {
        // 如果是主工程, 直接返回应用名称, 如果不是, 返回模块名称
        if (!this.name.contains("_")) {
            return "钱途无量"
        }
        return this.name
    }

internal fun buildTime(): String {
    return SimpleDateFormat("MM-dd-HH-mm", Locale.CHINA).format(Date())
}