buildscript {    repositories {        maven("https://jitpack.io")        mavenCentral()        google()        jcenter()    }    dependencies {        classpath("com.android.tools.build:gradle:${Version.gradle_tools}")        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}")        classpath("com.google.dagger:hilt-android-gradle-plugin:${Version.hilt}")        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Version.navigation}")    }}plugins {    `check-dependency-updates`}allprojects {    repositories {        maven("https://jitpack.io")        mavenCentral()        google()        jcenter()    }}task<Delete>("clean") {    delete(rootProject.buildDir)}