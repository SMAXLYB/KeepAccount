val isIntegrationMode: String by project

plugins {
    id("lib-config-plugin")
}

android {
    compileSdk = 30

    defaultConfig {
        minSdk = 21
        targetSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // arr包混淆
        // consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(Libs.kotlin)
    implementation(Libs.core_ktx)
    implementation(Libs.appcompat)
    testImplementation(Libs.junit)
    androidTestImplementation(Libs.junit_android)
    androidTestImplementation(Libs.espresso)
}