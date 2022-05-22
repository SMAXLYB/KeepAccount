object Sdk {
    const val compile_sdk_version = 31
    const val mini_sdk_version = 21
    const val target_sdk_version = 31
    const val version_code = 3
    const val version_name = "alpha-0.0.3"
}

/**
 * 版本号统一管理
 */
object Version {
    const val gradle_tools = "7.1.3"
    const val kotlin = "1.6.10"
    const val lifecycle = "2.4.1"
    const val room = "2.4.2"
    const val navigation = "2.4.1"
    const val gson = "2.9.0"
    const val compose = "1.1.1"
    const val leakCanary = "2.8.1"
    const val coroutines = "1.5.2"
    const val coil = "1.4.0"
    const val swipeRefreshLayout = "1.1.0"
    const val core_ktx = "1.7.0"
    const val appcompat = "1.4.1"
    const val junit = "4.13.2"
    const val junit_android = "1.1.3"
    const val espresso = "3.4.0"
    const val arouter = "1.5.2"
    const val recyclerview = "1.2.1"
    const val constraintLayout = "2.1.3"
    const val fragment_ktx = "1.4.1"
    const val lifecycle_ext = "2.2.0"
    const val material = "1.5.0"
    const val datastore = "1.0.0"
    const val autoService = "1.0.1"
    const val startup = "1.1.0"
    const val hilt = "2.40"//41有问题，不要升级
    const val autoSize = "1.2.1"
    const val mmkv = "1.2.13"
    const val bus = "1.7.3"
    const val chart = "3.1.0"
    const val dateTimePicker = "0.5.7"
    const val flexLayout = "3.0.0"
}

/**
 * 依赖库
 */
object Libs {
    // base
    const val core_ktx = "androidx.core:core-ktx:${Version.core_ktx}"
    const val appcompat = "androidx.appcompat:appcompat:${Version.appcompat}"

    // const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Version.recyclerview}"
    const val coil = "io.coil-kt:coil:${Version.coil}"
    const val gson = "com.google.code.gson:gson:${Version.gson}"
    const val swipeRefreshLayout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Version.swipeRefreshLayout}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Version.constraintLayout}"
    const val fragment_ktx = "androidx.fragment:fragment-ktx:${Version.fragment_ktx}"
    const val lifecycle_ext = "androidx.lifecycle:lifecycle-extensions:${Version.lifecycle_ext}"
    const val material = "com.google.android.material:material:${Version.material}"

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
    const val compose_activity = "androidx.activity:activity-compose:1.4.0"
    const val compose_viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07"

    // lifecycle
    const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycle}"
    const val lifecycle_livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.lifecycle}"
    const val lifecycle_viewModel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycle}"

    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Version.leakCanary}"

    // arouter
    const val arouter = "com.alibaba:arouter-api:${Version.arouter}"
    const val arouter_compiler = "com.alibaba:arouter-compiler:${Version.arouter}"

    // datastore
    const val datastore = "androidx.datastore:datastore-preferences:${Version.datastore}"

    // auto service
    const val auto_service = "com.google.auto.service:auto-service:${Version.autoService}"
    const val auto_service_annotations =
        "com.google.auto.service:auto-service-annotations:${Version.autoService}"

    // startup
    const val startup = "androidx.startup:startup-runtime:${Version.startup}"

    // hilt
    const val hilt = "com.google.dagger:hilt-android:${Version.hilt}"
    const val hilt_compiler = "com.google.dagger:hilt-android-compiler:${Version.hilt}"

    // autoSize
    const val autoSize = "com.github.JessYanCoding:AndroidAutoSize:${Version.autoSize}"

    // mmkv
    const val mmkv = "com.tencent:mmkv:${Version.mmkv}"

    // liveEventBus
    const val liveEventBus = "com.jeremyliao:live-event-bus-x:${Version.bus}"

    // MPAndroidChart
    const val mPAndroidChart = "com.github.PhilJay:MPAndroidChart:${Version.chart}"

    // DateTimePicker
    const val dateTimePicker = "com.github.loperSeven:DateTimePicker:${Version.dateTimePicker}"

    // flexLayout
    const val flexLayout = "com.google.android.flexbox:flexbox:${Version.flexLayout}"
}

