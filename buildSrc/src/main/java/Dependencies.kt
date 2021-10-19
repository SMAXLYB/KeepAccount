object Sdk {
    const val compile_sdk_version = 30
    const val mini_sdk_version = 21
    const val target_sdk_version = 30
    const val version_code = 2
    const val version_name = "alpha-0.0.1"
}

/**
 * 版本号统一管理
 */
object Version {
    const val kotlin = "1.5.31"
    const val lifecycle = "2.3.1"
    const val room = "2.3.0"
    const val navigation = "2.3.5"
    const val gson = "2.8.6"
    const val compose = "1.0.4"
    const val leakCanary = "2.7"
    const val coroutines = "1.5.0"
    const val coil = "1.2.1"
    const val swipeRefreshLayout = "1.1.0"
    const val core_ktx = "1.6.0"
    const val appcompat = "1.3.0"
    const val junit = "4.13.2"
    const val junit_android = "1.1.3"
    const val espresso = "3.4.0"
    const val arouter = "1.5.2"
    const val recyclerview = "1.2.1"
    const val constraintLayout = "2.0.4"
}

/**
 * 依赖库
 */
object Libs {
    // base
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin}"
    const val core_ktx = "androidx.core:core-ktx:${Version.core_ktx}"
    const val appcompat = "androidx.appcompat:appcompat:${Version.appcompat}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Version.recyclerview}"
    const val coil = "io.coil-kt:coil:${Version.coil}"
    const val gson = "com.google.code.gson:gson:${Version.gson}"
    const val swipeRefreshLayout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Version.swipeRefreshLayout}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Version.constraintLayout}"

    // test
    const val junit = "junit:junit:${Version.junit}"
    const val junit_android = "androidx.test.ext:junit:${Version.junit_android}"
    const val espresso = "androidx.test.espresso:espresso-core:${Version.espresso}"

    // room
    const val room_runtime = "androidx.room:room-runtime:${Version.room}"
    const val room_compiler = "androidx.room:room-compiler:${Version.room}"
    const val room = "androidx.room:room-ktx:${Version.room}"

    // navigation
    const val navigation_fragment =
        "androidx.navigation:navigation-fragment-ktx:${Version.navigation}"
    const val navigation_ui = "androidx.navigation:navigation-ui-ktx:${Version.navigation}"

    // compose
    const val compose_ui = "androidx.compose.ui:ui:${Version.compose}"
    const val compose_tools = "androidx.compose.ui:ui-tooling:${Version.compose}"
    const val compose_foundation = "androidx.compose.foundation:foundation:${Version.compose}"
    const val compose_material = "androidx.compose.material:material:${Version.compose}"
    const val compose_compiler = "androidx.compose.compiler:compiler:${Version.compose}"
    const val compose_runtime = "androidx.compose.runtime:runtime:${Version.compose}"
    const val compose_livedata = "androidx.compose.runtime:runtime-livedata:${Version.compose}"
    const val compose_activity = "androidx.activity:activity-compose:1.3.1"
    const val compose_viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07"

    // lifecycle
    const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycle}"
    const val lifecycle_livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.lifecycle}"
    const val lifecycle_viewModel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycle}"

    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Version.leakCanary}"

    const val coil = "io.coil-kt:coil:${Version.coil}"

    const val gson = "com.google.code.gson:gson:${Version.gson}"

    const val swiperefreshlayout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Version.swiperefreshlayout}"

    // arouter
    const val arouter = "com.alibaba:arouter-api:${Version.arouter}"
    const val arouter_compiler = "com.alibaba:arouter-compiler:${Version.arouter}"
}

