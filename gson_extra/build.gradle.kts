plugins {
    id("com.android.library")
}

android {
    compileSdkVersion(AppConfig.compileSdkVersion)
    buildToolsVersion(AppConfig.buildToolsVersion)

    defaultConfig {
        minSdkVersion(AppConfig.minSdkVersion)
        targetSdkVersion(AppConfig.targetSdkVersion)

        // testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
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

// task publishJar (type: Jar)
//
// task(type:Jar::class.java){}
//
// // 配置发布的构建类型为jar
// artifacts {
//     archives publishJar
// }
//
// group "com.example.gson.extra"
// version "1.0.0"
//
// uploadArchives {
//     repositories {
//         // 发布到本地文件夹
//         flatDir {
//             name("libs")
//             dirs("$projectDir/libs")
//         }
//
//         // 发布到本地maven库 路径c:/user/.m2/rep
//         mavenLocal()
//
//         // 发布到线上maven私服
//         // MavenDeployer{
//         //     repositories(url){
//         //
//         //     }
//         //     snapshotRepository(url){
//         //
//         //     }
//         // }
//     }
// }