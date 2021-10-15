import java.util.*
import java.text.SimpleDateFormat as DateFormat

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = 30

    // 渠道默认使用的配置
    defaultConfig {
        applicationId = "life.chenshi.keepaccounts"
        minSdk = 21
        targetSdk = 30
        versionCode = AppVersionConfig.versionCode
        versionName = AppVersionConfig.versionName

        // 声明多维度 对flavor进行分组
        flavorDimensions.add("normal")
        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    // 签名配置
    signingConfigs {
        register("release") {
            storeFile = file("../key/keepaccount.jks")
            storePassword = "19980909a"
            keyAlias = "keepaccount"
            keyPassword = "19980909a"

            enableV1Signing = true
            enableV2Signing = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            // 签名配置
            signingConfig = signingConfigs.getByName("release")
            // 自动清理未使用资源 开启minifyEnabled有效
            // shrinkResources true
            // 对齐
            // zipAlignEnabled true
        }

        getByName("debug") {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
        }
    }

    // 多渠道
    productFlavors {
        // 正常包
        create("normal") {
            // 分类到normal组下
            dimension = "normal"
            // manifest占位
            // manifestPlaceholders.put("CHANNEL","normal")
            // buildConfig动态配置
            // buildConfigField "String","INDEX_URL",""http://chenshi.life""
            // 资源动态配置
            // resValue "string","app_name","Keep-Account"
            // 包名
            // applicationId "com.example.keepaccount"
            // 发布携带混淆规则
            // consumerProguardFiles "proguard-rules.pro","proguard-android.txt"
        }
    }

    // productFlavors.all{ flavor ->
    //     manifestPlaceholders.put("CHANNEL","normal")
    // }

    lint {
        isAbortOnError = false
        isCheckReleaseBuilds = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
        // 集成环境关闭
        // useIR = true
    }
    buildFeatures {
        dataBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Version.compose
    }

    applicationVariants.all {
        val buildType = this.buildType.name
        val versionName = this.versionName
        outputs.all {

            // 如果是apk文件
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                if (buildType == "release") {
                    val fileName = "Release_KeepAccount_${flavorName}_${versionName}_${buildTime()}.apk"
                    // 修改apk名称
                    this.outputFileName = fileName
                    // 修改生成路径
                    // this.getPackageApplication().outputDirectory = new File ("../apks")
                }
            }
        }
    }
}

dependencies {
    /**-------------------libs依赖-----------------------**/
    implementation(fileTree("dir" to "libs", "include" to arrayOf("*.jar")))
    /**-------------------module依赖---------------------**/
    implementation(
        project(":gson_extra")
    )

    /**-------------------第三方库依赖--------------------**/
    // 基本库
    implementation(Libs.kotlin)
    implementation(Libs.core_ktx)
    implementation(Libs.appcompat)
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")

    // viewModel
    implementation(Libs.lifecycle_viewmodel)
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.fragment:fragment-ktx:1.3.5")
    // navigation
    implementation(Libs.navigation_ui)
    implementation(Libs.navigation_fragment)
    // lifeCycle
    implementation(Libs.lifecycle)
    //liveData
    implementation(Libs.lifecycle_livedata)
    // room
    implementation(Libs.room_runtime)
    kapt(Libs.room_compiler)
    implementation(Libs.room)
    // 协程
    implementation(Libs.coroutines)
    implementation(Libs.swiperefreshlayout)
    // 图片加载
    // implementation "com.github.bumptech.glide:glide:4.11.0"
    // implementation("androidx.legacy:legacy-support-v4:1.0.0")
    // kapt ("com.github.bumptech.glide:compiler:4.11.0"
    implementation(Libs.coil)

    // 时间选择
    implementation("com.github.loperSeven:DateTimePicker:0.3.1")
    // 图表
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    // dataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0-rc01")
    // recyclerview
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    // 内存泄漏
    debugImplementation(Libs.leakCanary)
    // liveDataBus
    implementation("com.jeremyliao:live-event-bus-x:1.7.3")
    // compose
    implementation(Libs.compose_ui)
    implementation(Libs.compose_tools)
    implementation(Libs.compose_foundation)
    implementation(Libs.compose_material)
    implementation(Libs.compose_compiler)
    implementation(Libs.compose_runtime)
    implementation(Libs.compose_livedata)
    implementation(Libs.compose_activity)
    implementation(Libs.compose_viewmodel)

}

fun buildTime(): String {
    return DateFormat("yyyy-MM-dd-HH-mm").format(Date())
}