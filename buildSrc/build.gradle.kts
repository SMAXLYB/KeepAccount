plugins {
    `kotlin-dsl`
}
gradlePlugin {
    plugins {
        create("AppConfigPlugin") {
            id = "app-config-plugin"
            implementationClass = "life.chenshi.keepaccounts.plugins.AppConfigPlugin"
        }
        create("LibConfigPlugin") {
            id = "lib-config-plugin"
            implementationClass = "life.chenshi.keepaccounts.plugins.LibConfigPlugin"
        }
    }
}
repositories{
    maven("https://jitpack.io")
    mavenCentral()
    google()
    jcenter()
}
dependencies{
    /* Depend on the android gradle plugin, since we want to access it in our plugin */
    implementation("com.android.tools.build:gradle:7.0.0")

    /* Depend on the kotlin plugin, since we want to access it in our plugin */
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")

    /* Depend on the default Gradle API's since we want to build a custom plugin */
    implementation(gradleApi())
    implementation(localGroovy())
}