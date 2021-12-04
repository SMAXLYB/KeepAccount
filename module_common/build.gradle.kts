plugins {
    id("lib-config-plugin")
}

android {
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    // viewModel
    api(Libs.lifecycle_viewModel)
    api(Libs.fragment_ktx)
    api(Libs.core_ktx)
    api(Libs.appcompat)
    api(Libs.constraintLayout)
    api(Libs.recyclerview)
    api(Libs.datastore)
    api(Libs.auto_service)
    api(Libs.autoSize)
    api(Libs.mmkv)
    implementation(Libs.material)
}