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


/**
 * android块配置
 */
internal fun Project.applyAndroidBlock(runAsApp: Boolean) {
    println("--->  ${project.name}配置android ")
    if (runAsApp) {
        extensions.getByType<BaseAppModuleExtension>().applyConfig()
    } else {
        extensions.getByType<LibraryExtension>().applyConfig()
    }

    extensions.configure<KaptExtension>("kapt") {
        arguments {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}

internal fun BaseAppModuleExtension.applyConfig() {
    compileSdk = Sdk.compile_sdk_version

    defaultConfig {
        minSdk = Sdk.mini_sdk_version
        targetSdk = Sdk.target_sdk_version
        versionCode = Sdk.version_code
        versionName = Sdk.version_name
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // 签名配置
    signingConfigs {
        register("release") {
            storeFile = File("../key/keepaccount.jks")
            storePassword = "19980909a"
            keyAlias = "keepaccount"
            keyPassword = "19980909a"

            enableV1Signing = true
            enableV2Signing = true
        }
    }

    buildTypes {
        release {
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
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            applicationIdSuffix = ".debug"
        }
    }

    lint {
        isAbortOnError = false
        isCheckReleaseBuilds = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
        jvmTarget = "11"
    }
}

internal fun LibraryExtension.applyConfig() {
    compileSdk = Sdk.compile_sdk_version

    defaultConfig {
        minSdk = Sdk.mini_sdk_version
        targetSdk = Sdk.target_sdk_version
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            // 自动清理未使用资源 开启minifyEnabled有效
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    lint {
        isAbortOnError = false
        isCheckReleaseBuilds = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
        jvmTarget = "11"
    }
}