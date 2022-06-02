plugins {
    id("app-config-plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
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

    implementation(Libs.swipeRefreshLayout)

    // 内存泄漏
    debugImplementation(Libs.leakCanary)

    // compose
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