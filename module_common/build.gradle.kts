plugins {
    id("lib-config-plugin")
}

android {
    dataBinding {
        isEnabled = true
    }
}

dependencies {
    api(Libs.core_ktx)
    api(Libs.appcompat)
    api(Libs.recyclerview)
}