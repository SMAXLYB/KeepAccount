plugins {
    id ("com.android.library")
    id ("kotlin-android")
}

android {

    defaultConfig {
        // testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles ("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation (Libs.kotlin)
    implementation (Libs.core_ktx)
    implementation (Libs.appcompat)
}