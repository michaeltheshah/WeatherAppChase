plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    compileSdk = 34

    defaultConfig {
        namespace = "com.mshaw.weatherappchase"
        applicationId = "com.mshaw.weatherappchase"
        minSdk = 21
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "META-INF/*"
        }
    }
    tasks.withType<Test> {
        useJUnitPlatform()
    }
    testOptions {
        unitTests.isIncludeAndroidResources  = true
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {

    // Accompanist
    implementation("com.google.accompanist:accompanist-permissions:0.33.2-alpha")

    implementation("com.google.android.gms:play-services-location:21.0.1")
    val activityComposeVersion = "1.7.2"

    // AndroidX
    implementation("androidx.compose.ui:ui:1.5.1")
    implementation("androidx.compose.material:material:1.5.1")
    implementation("androidx.activity:activity-compose:$activityComposeVersion")

    // Coil
    val coilVersion = "2.4.0"
    implementation("io.coil-kt:coil-compose:$coilVersion")

    // Compose
    val composeBom = platform("androidx.compose:compose-bom:2022.10.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    val composeVersion = "1.5.1"

    // UI
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")

    // Android Studio Preview Support
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")

    // UI Tests
    implementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")

    // ViewModel
    val lifecycleViewModelComposeVersion = "2.6.2"
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleViewModelComposeVersion")

    // Lifecycle
    val lifecycleRuntimeComposeVersion = "2.6.2"
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleRuntimeComposeVersion")

    // Navigation
    val navigationComposeVersion = "2.7.3"
    implementation("androidx.navigation:navigation-compose:$navigationComposeVersion")

    // Navigation (Hilt)
    val hiltNavigationComposeVersion = "1.0.0"
    implementation("androidx.hilt:hilt-navigation-compose:$hiltNavigationComposeVersion")

    //Core
    val coreVersion = "1.12.0"
    implementation("androidx.core:core-ktx:$coreVersion")

    //Lifecycle
    val lifecycleVersion = "2.6.2"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")

    // Hilt
    val hiltVersion = "2.48"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    ksp("com.google.dagger:hilt-android-compiler:$hiltVersion")

    // Retrofit
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // OkHttp
    val okHttpVersion = "4.11.0"
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")

    //kotlinx.serialization
    val serializationVersion = "1.6.0"
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    // Room
    val roomVersion = "2.5.2"

    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    androidTestImplementation("androidx.room:room-testing:$roomVersion")

    // Testing
    val junitVersion = "4.13.2"
    testImplementation("junit:junit:$junitVersion")

    testImplementation("androidx.arch.core:core-testing:2.2.0")

    // Jupiter
    val jupiterVersion = "5.8.2"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")

    val extVersion = "1.1.5"
    androidTestImplementation("androidx.test.ext:junit:$extVersion")

    val mockkVersion = "1.13.3"
    implementation("io.mockk:mockk:$mockkVersion")

    // Module
    implementation(project(":data"))
}