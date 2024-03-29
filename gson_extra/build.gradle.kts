
plugins {
    id("com.android.library")
    id("maven-publish")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 21
        targetSdk = 31

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
        maven(url = uri(layout.buildDirectory.dir("$projectDir/libs")))
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