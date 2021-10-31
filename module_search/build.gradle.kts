plugins {
    id("lib-config-plugin")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(Libs.hilt)
    kapt(Libs.hilt_compiler)
}