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
    // 0.39.0有问题, 不要升级
    implementation("com.github.ben-manes:gradle-versions-plugin:0.38.0")

    /* Depend on the android gradle plugin, since we want to access it in our plugin */
    implementation("com.android.tools.build:gradle:7.1.3")

    /* Depend on the kotlin plugin, since we want to access it in our plugin */
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")

    /* Depend on the default Gradle API's since we want to build a custom plugin */
    implementation(gradleApi())
    implementation(localGroovy())
}