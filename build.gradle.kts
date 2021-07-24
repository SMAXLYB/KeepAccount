buildscript {
    repositories {
        maven("https://jitpack.io")
        mavenCentral()
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5")
    }
}

allprojects {
    repositories {
        maven("https://jitpack.io")
        mavenCentral()
        google()
        jcenter()
    }
}

subprojects {
    afterEvaluate {
        if (extensions.getByName("android") is (com.android.build.gradle.LibraryExtension)) {
            extensions.configure<com.android.build.gradle.LibraryExtension>(
                "android"
            ) {
                buildToolsVersion(AppConfig.buildToolsVersion)
                compileSdkVersion(AppConfig.compileSdkVersion)

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
                }
                defaultConfig {
                    minSdkVersion(AppConfig.minSdkVersion)
                    targetSdkVersion(AppConfig.targetSdkVersion)
                }
            }
        } else {
            extensions.configure<com.android.build.gradle.internal.dsl.BaseAppModuleExtension>(
                "android"
            ) {
                buildToolsVersion(AppConfig.buildToolsVersion)
                compileSdkVersion(AppConfig.compileSdkVersion)

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
                }

                defaultConfig {
                    minSdkVersion(AppConfig.minSdkVersion)
                    targetSdkVersion(AppConfig.targetSdkVersion)
                    versionCode(AppConfig.versionCode)
                    versionName(AppConfig.versionName)
                }
            }
        }
    }

}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}