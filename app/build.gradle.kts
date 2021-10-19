plugins {
    id("app-config-plugin")
    id("androidx.navigation.safeargs.kotlin")
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

    kotlinOptions {
        // 集成环境关闭
        // useIR = true
    }
    buildFeatures {
        dataBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Version.compose
    }
}

dependencies {
    /**-------------------libs依赖-----------------------**/
    implementation(fileTree("dir" to "libs", "include" to arrayOf("*.jar")))
    /**-------------------module依赖---------------------**/
    implementation(project(":gson_extra"))
    implementation(project(":module_common"))

    /**-------------------第三方库依赖--------------------**/
    // 基本库
    implementation(Libs.kotlin)
    implementation("com.google.android.material:material:1.4.0")
    implementation(Libs.constraintLayout)

    // viewModel
    implementation(Libs.lifecycle_viewModel)
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.fragment:fragment-ktx:1.3.5")
    implementation(Libs.navigation_ui)
    implementation(Libs.navigation_fragment)
    implementation(Libs.lifecycle)
    implementation(Libs.lifecycle_livedata)
    implementation(Libs.room_runtime)
    implementation(Libs.room)
    kapt(Libs.room_compiler)
    implementation(Libs.coroutines)
    implementation(Libs.swipeRefreshLayout)
    implementation(Libs.coil)

    // 时间选择
    implementation("com.github.loperSeven:DateTimePicker:0.3.1")
    // 图表
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    // dataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0-rc01")
    // 内存泄漏
    debugImplementation(Libs.leakCanary)
    // liveDataBus
    implementation("com.jeremyliao:live-event-bus-x:1.7.3")
    implementation(Libs.compose_ui)
    implementation(Libs.compose_tools)
    implementation(Libs.compose_foundation)
    implementation(Libs.compose_material)
    implementation(Libs.compose_compiler)
    implementation(Libs.compose_runtime)
    implementation(Libs.compose_livedata)
    implementation(Libs.compose_activity)
    implementation(Libs.compose_viewModel)
}