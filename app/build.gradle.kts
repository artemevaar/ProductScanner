plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.gms)
  id ("kotlin-kapt")
}

android {
    namespace = "com.example.productscanner"
    compileSdk = 34
    //compileSdk = 35

    defaultConfig {
        applicationId = "com.example.productscanner"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
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

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)



    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //zxing
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation ("com.google.zxing:core:3.3.3")


    // camera
    implementation("androidx.camera:camera-lifecycle:1.3.4")
    implementation("androidx.camera:camera-camera2:1.3.4")
    implementation("androidx.camera:camera-view:1.3.4")

    implementation("io.coil-kt:coil-compose:2.1.0")

    //navigation
    implementation ("androidx.navigation:navigation-compose:2.6.0")

    //swiperefresh
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")


    //апи
    implementation("com.android.volley:volley:1.2.1")
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    // Gson Converter (для обработки JSON)
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.8.9")

    //бд firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore.ktx)


    //room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:$room_version")
    implementation ("com.google.guava:guava:30.1-android")

    //test
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.0.5")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    debugImplementation ("androidx.compose.ui:ui-tooling:1.0.5")
    debugImplementation ("androidx.compose.ui:ui-test-manifest:1.0.5")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

}