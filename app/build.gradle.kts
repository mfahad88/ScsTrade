plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.scstrade"
    compileSdk = 35
    buildFeatures{
        viewBinding=true
        compose=true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    defaultConfig {
        applicationId = "com.scstrade.scstradepro"
        minSdk = 23
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {
    implementation(project(":MyCalendar-sdk"))
    implementation(libs.firebase.auth.ktx)
    val room_version = "2.6.1"

//    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
    // See Add the KSP plugin to your project
    kapt("androidx.room:room-compiler:$room_version")

    // If this project only uses Java source, use the Java annotationProcessor
    // No additional plugins are necessary
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    /*implementation ("androidx.navigation:navigation-fragment-ktx:2.8.5")
    implementation ("androidx.navigation:navigation-ui-ktx:2.8.5")*/
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.airbnb.android:lottie:3.4.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation (libs.retrofit)
    implementation (libs.retrofit2.converter.gson)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.coordinatorlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    implementation ("androidx.compose.ui:ui:1.7.7")
    implementation ("androidx.compose.material:material:1.7.7")
//    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
//    implementation ("com.prof18.rssparser:rssparser:1.1")

    implementation ("com.fasterxml.jackson.core:jackson-core:2.18.3")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.18.3")
    implementation ("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.18.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4")
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("javax.xml.stream:stax-api:1.0")
    implementation ("com.google.android.gms:play-services-auth:21.0.0")
    implementation ("com.facebook.android:facebook-login:[8,9)")
    implementation ("com.github.GoodieBag:Pinview:v1.4")
}