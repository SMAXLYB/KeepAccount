plugins {
    id("lib-config-plugin")
}

android {
    // 渠道默认使用的配置
    defaultConfig {

        // room配置, 需要则配，不需要不配
        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    // viewModel
    api(Libs.lifecycle_viewModel)
    api(Libs.fragment_ktx)
    api(Libs.appcompat)
    api(Libs.constraintLayout)
    api(Libs.recyclerview)
    api(Libs.datastore)
    api(Libs.auto_service)
    api(Libs.autoSize)
    api(Libs.mmkv)
    api(Libs.gson)
    api(Libs.material)
    api(Libs.lifecycle)
    api(Libs.lifecycle_livedata)
    api(Libs.navigation_ui)
    api(Libs.navigation_fragment)
    api(Libs.liveEventBus)
    api(Libs.dateTimePicker)
    implementation(project(":gson_extra"))
    api(Libs.room_runtime)
    implementation(Libs.room)
    kapt(Libs.room_compiler)
    api(Libs.coil)
}