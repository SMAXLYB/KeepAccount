object Version {
    const val lifecycle = "2.3.0"
    const val room = "2.2.6"
    const val navigation = "2.3.1"
    const val gson = "2.8.6"
}

object Libs {
    const val room_runtime = "androidx.room:room-runtime:${Version.room}"
    const val room_compiler = "androidx.room:room-compiler:${Version.room}"
    const val room = "androidx.room:room-ktx:${Version.room}"


    const val navigation_fragment = "androidx.navigation:navigation-fragment-ktx:${Version.navigation}"
    const val navigation_ui = "androidx.navigation:navigation-ui-ktx:${Version.navigation}"

    const val gson = "com.google.code.gson:gson:${Version.gson}"
}