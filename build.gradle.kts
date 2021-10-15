buildscript {
    repositories {
        addRepositories()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0")
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
        extensions.configure<com.android.build.gradle.BaseExtension>("android") {
            buildToolsVersion(AppVersionConfig.buildToolsVersion)
            compileSdkVersion(AppVersionConfig.compileSdkVersion)

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }

            defaultConfig {
                minSdk = AppVersionConfig.minSdkVersion
                targetSdk = AppVersionConfig.targetSdkVersion
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