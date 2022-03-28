plugins {
    id("lib-config-plugin")
    id("dagger.hilt.android.plugin")
}
android{
    buildFeatures {
        dataBinding = true
    }
}
dependencies {
    implementation(Libs.hilt)
    kapt(Libs.hilt_compiler)
}