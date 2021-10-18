plugins {
    id("lib-config-plugin")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    compileSdk = 30

    defaultConfig {
        minSdk = 21
        targetSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // consumerProguardFiles("consumer-rules.pro")

        // 每个模块都要
        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }
    }

    // 每个模块都要
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    // 每个模块都要
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    // 每个模块都要
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation (Libs.kotlin)
    implementation (Libs.core_ktx)
    implementation (Libs.appcompat)
    testImplementation(Libs.junit)
    androidTestImplementation(Libs.junit_android)
    androidTestImplementation(Libs.espresso)

    // 所有组件依赖common
    api(Libs.arouter)
    // 每个模块都要kapt
    kapt(Libs.arouter_compiler)
}