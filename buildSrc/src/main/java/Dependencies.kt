/**
 * 版本号统一管理
 */
object Version {
    const val kotlin = "1.5.10"
    const val lifecycle = "2.3.0"
    const val room = "2.3.0"
    const val navigation = "2.3.1"
    const val gson = "2.8.6"
    const val compose = "1.0.0-rc02"
}

/**
 * 依赖库
 */
object Libs {
    const val room_runtime = "androidx.room:room-runtime:${Version.room}"
    const val room_compiler = "androidx.room:room-compiler:${Version.room}"
    const val room = "androidx.room:room-ktx:${Version.room}"

    const val navigation_fragment = "androidx.navigation:navigation-fragment-ktx:${Version.navigation}"
    const val navigation_ui = "androidx.navigation:navigation-ui-ktx:${Version.navigation}"

    const val gson = "com.google.code.gson:gson:${Version.gson}"

    const val compose_ui = "androidx.compose.ui:ui:${Version.compose}"
    const val compose_tools = "androidx.compose.ui:ui-tooling:${Version.compose}"
    const val compose_foundation ="androidx.compose.foundation:foundation:${Version.compose}"
    const val compose_material = "androidx.compose.material:material:${Version.compose}"
    const val compose_compiler ="androidx.compose.compiler:compiler:${Version.compose}"
    const val compose_runtime = "androidx.compose.runtime:runtime:${Version.compose}"
    const val compose_livedata = "androidx.compose.runtime:runtime-livedata:${Version.compose}"
    const val compose_activity = "androidx.activity:activity-compose:1.3.0-alpha08"
    const val compose_viewmodel = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha05"
}