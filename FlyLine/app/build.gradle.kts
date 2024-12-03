plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.zdy.flyline"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.zdy.flyline"
        minSdk = 22
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures{
        viewBinding = true
    }

}

dependencies {


    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    // ----- Hilt ------ //
    val daggerHiltVersion = "2.51.1"
    implementation (group= "javax.inject", name= "javax.inject", version= "1")

    implementation("com.google.dagger:hilt-android:${daggerHiltVersion}")
    kapt("com.google.dagger:hilt-android-compiler:${daggerHiltVersion}")
    //-------------------//

    // --- Coroutines --- //
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    //------------------//

    // --- Coroutine Lifecycle Scopes --- //
    val coroutineLifecycleScopesVersion = "2.8.7"
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:${coroutineLifecycleScopesVersion}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${coroutineLifecycleScopesVersion}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${coroutineLifecycleScopesVersion}")
    implementation("androidx.fragment:fragment-ktx:1.8.5")
    //------------------------------------//



    // -- Navigation -- //
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.3")
    // ---------------- //

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}