plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.androidstoreapp.core.network"
    compileSdk = 36
    defaultConfig { minSdk = 24 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":domain"))
    api(libs.retrofit)
    api(libs.retrofit.gson)
    api(libs.okhttp.logging)
    implementation(libs.coroutines.android)
    testImplementation(libs.bundles.test.unit)
}
