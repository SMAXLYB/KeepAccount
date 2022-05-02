plugins {
    id("lib-config-plugin")
}
android {
    buildFeatures {
        dataBinding = true
    }
}
dependencies {
    implementation(Libs.mPAndroidChart)
}