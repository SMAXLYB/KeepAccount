plugins {
    id("lib-config-plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    buildFeatures {
        dataBinding = true
    }
}
dependencies {
    implementation(Libs.hilt)
    kapt(Libs.hilt_compiler)
}