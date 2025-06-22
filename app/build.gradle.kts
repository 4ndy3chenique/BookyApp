plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin) // Firebase plugin
}

android {
    namespace = "com.andyechenique.booky"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.andyechenique.booky"
        minSdk = 25
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.maps)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Import the Firebase BoM (Bill of Materials)
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))

    // Firebase dependencies (no version needed gracias a BoM)
    implementation("com.google.firebase:firebase-auth")        // Autenticación por correo, Google, etc.
    implementation("com.google.firebase:firebase-firestore")   // Firestore para almacenar datos del usuario
    implementation("com.google.firebase:firebase-analytics")   // (Opcional) Analytics de Firebase

    // Also add the dependencies for the Credential Manager libraries and specify their versions
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")  //  Google
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    implementation("com.google.android.gms:play-services-auth:21.2.0") // Es necesario agregar esto explícitamente para Google Sign-in

    implementation ("com.facebook.android:facebook-login:latest.release")

    implementation ("com.loopj.android:android-async-http:1.4.11")


}
