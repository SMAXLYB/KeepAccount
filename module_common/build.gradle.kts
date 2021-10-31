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
    api(Libs.appcompat)
    api(Libs.constraintLayout)
    api(Libs.recyclerview)
    api(Libs.datastore)
    api(Libs.auto_service)
    implementation(Libs.material)
}