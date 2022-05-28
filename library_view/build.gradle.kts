plugins {
    id("lib-config-plugin")
}

android {
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    api(Libs.core_ktx)
}