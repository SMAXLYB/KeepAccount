plugins {
    id("com.android.library")
    id("maven-publish")
}

android {

    defaultConfig {
        // testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    // gson
    api(Libs.gson)
}

val androidSourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
}

publishing {
    repositories {
        mavenLocal()
        maven {
            url = uri(layout.buildDirectory.dir("$projectDir/libs"))
        }
    }

    publications {
        create<MavenPublication>("jar") {
            groupId = "life.chenshi"
            artifactId = "gson_extra"
            version = "1.0.0"
            artifact(androidSourcesJar.get())
        }
    }
}