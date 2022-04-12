plugins {
    id("lib-config-plugin")
    id("androidx.navigation.safeargs.kotlin")
}
android {
    buildFeatures {
        dataBinding = true
    }
}
dependencies {
    implementation(Libs.dateTimePicker)
}