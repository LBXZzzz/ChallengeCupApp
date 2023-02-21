plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    compileSdk = Build.compileSdk

    defaultConfig {
        applicationId = Build.applicationId
        minSdk = Build.minSdk
        targetSdk = Build.targetSdk
        versionCode = Build.versionCode
        versionName = Build.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    dataBinding {
        isEnabled = true
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":ble"))

    implementation(Dependencies.KotlinX.coroutines)

    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.AndroidX.Navigation.fragment)
    implementation(Dependencies.AndroidX.Navigation.ui)
    kapt(Dependencies.AndroidX.Room.kapt)
    implementation(Dependencies.AndroidX.Room.runtime)
    implementation(Dependencies.AndroidX.Room.ktx)
    implementation(Dependencies.AndroidX.DataStore.preference)
    implementation(Dependencies.View.constraintLayout)
    implementation(Dependencies.RikkaX.appCompat)
    implementation(Dependencies.RikkaX.material)
    implementation(Dependencies.RikkaX.materialPreference)
    implementation(Dependencies.RikkaX.core)
    implementation(Dependencies.RikkaX.insets)
    implementation(Dependencies.RikkaX.mainSwitchBar)
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.4.+")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    testImplementation(Dependencies.Test.junit)
    androidTestImplementation(Dependencies.AndroidTest.junit)
    androidTestImplementation(Dependencies.AndroidTest.espresso)
    implementation(Dependencies.Banner.banner1)
}

configurations.all {
    exclude("androidx.appcompat", "appcompat")
}