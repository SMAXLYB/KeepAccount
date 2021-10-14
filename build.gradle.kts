buildscript {
    repositories {
        addRepositories()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5")
    }
}

allprojects {
    repositories {
        addRepositories()
    }
}

subprojects {
    afterEvaluate {
        extensions.configure<com.android.build.gradle.BaseExtension>(
            "android"
        ) {
            buildToolsVersion(AppVersionConfig.buildToolsVersion)
            compileSdkVersion(AppVersionConfig.compileSdkVersion)

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

            defaultConfig {
                minSdkVersion(AppVersionConfig.minSdkVersion)
                targetSdkVersion(AppVersionConfig.targetSdkVersion)
                // 如果是app工程
                if (extensions.getByName("android") is (com.android.build.gradle.internal.dsl.BaseAppModuleExtension)) {
                    versionCode(AppVersionConfig.versionCode)
                    versionName(AppVersionConfig.versionName)
                }
            }
        }
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}