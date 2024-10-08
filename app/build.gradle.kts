plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.ksp)
    id("dagger.hilt.android.plugin")
}


android {
    namespace = "com.shong.compose_mvi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.shong.compose_mvi"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

//    signingConfigs {
//        create("signedKey")
//         {
//            storeFile = file('../storeFile.keystore')
//            storePassword = 'storePassword'
//            keyAlias = 'keyAlias'
//            keyPassword = 'keyPassword'
//        }
//    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
//            signingConfig = signingConfigs.getByName("signedKey")
            signingConfig = signingConfigs.getByName("debug")
            resValue("string", "app_name", "Compose_MVI")
        }
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            resValue("string", "app_name", "Compose_MVI_debug")
        }
    }

    flavorDimensions += "server"
    productFlavors {          // 2
        create("Live") {
            dimension = "server"
            buildConfigField("String", "BASE_URL", "\"http:/worldtimeapi.org/api/\"")
        }
        create("Develop") {
            dimension = "server"
            buildConfigField("String", "BASE_URL", "\"http://worldtimeapi.org/api/\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

composeCompiler {
    enableStrongSkippingMode = true

    reportsDestination = layout.buildDirectory.dir("compose_compiler")
//    stabilityConfigurationFile = rootProject.layout.projectDirectory.file("stability_config.conf")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.hilt)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler.ksp)

    implementation(libs.retrofit2)
    implementation(libs.retrofit2.gson)

    implementation(libs.okhttp3)
    implementation(libs.okhttp3.logging.interceptor)

    implementation(libs.room.rumtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler.ksp)
}