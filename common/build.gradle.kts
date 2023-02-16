plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = Build.compileSdk

    defaultConfig {
        minSdk = Build.minSdk
        targetSdk = Build.targetSdk

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

    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.RikkaX.appCompat)
    implementation(Dependencies.RikkaX.material)
    implementation(Dependencies.RikkaX.materialPreference)
    implementation(Dependencies.RikkaX.core)
    testImplementation(Dependencies.Test.junit)
    androidTestImplementation(Dependencies.AndroidTest.junit)
    androidTestImplementation(Dependencies.AndroidTest.espresso)
}

configurations.all {
    exclude("androidx.appcompat", "appcompat")
}