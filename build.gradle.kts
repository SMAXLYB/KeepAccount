buildscript {    repositories {        maven("https://jitpack.io")        mavenCentral()        google()        jcenter()    }    dependencies {        classpath("com.android.tools.build:gradle:7.0.0")        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")        classpath("com.google.dagger:hilt-android-gradle-plugin:2.28-alpha")        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5")    }}plugins {    `check-dependency-updates`}allprojects {    repositories {        maven("https://jitpack.io")        mavenCentral()        google()        jcenter()    }}task<Delete>("clean") {    delete(rootProject.buildDir)}