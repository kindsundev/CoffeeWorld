import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
}

android {
    namespace = CoffeeWordNames.namespace
    compileSdk = CoffeeWordVersions.compileSdk

    defaultConfig {
        applicationId = CoffeeWordNames.applicationId
        minSdk = CoffeeWordVersions.minSdk
        targetSdk = CoffeeWordVersions.targetSdk
        versionCode = CoffeeWordVersions.versionCode
        versionName = CoffeeWordVersions.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        buildConfigField("String", "TOKEN_SECRET", "\"${properties.getProperty("TOKEN_SECRET")}\"")
        buildConfigField("String", "API_BASE_URL", "\"${properties.getProperty("API_BASE_URL")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = CoffeeWordVersions.jvmTarget
    }
    buildFeatures {
        dataBinding = true
        buildConfig = true
    }
    kapt {
        correctErrorTypes = true
    }
}


dependencies {
    
    implementation(CoffeeWordDepencencies.coreKtx)
    implementation(CoffeeWordDepencencies.appCompat)
    implementation(CoffeeWordDepencencies.material)
    implementation(CoffeeWordDepencencies.constraintLayout)
    testImplementation(CoffeeWordDepencencies.junit)
    androidTestImplementation(CoffeeWordDepencencies.extJunit)
    androidTestImplementation(CoffeeWordDepencencies.espressoCore)

    implementation(CoffeeWordDepencencies.hilt)
    kapt(CoffeeWordDepencencies.kaptHilt)

    implementation(CoffeeWordDepencencies.viewModelKtx)

    implementation(CoffeeWordDepencencies.liveDataKtx)

    implementation(CoffeeWordDepencencies.retrofit)
    implementation(CoffeeWordDepencencies.retrofitConverterGson)
    
    implementation(CoffeeWordDepencencies.okhttp3)
    implementation(CoffeeWordDepencencies.okhttp3LoggingInterceptor)

    implementation(CoffeeWordDepencencies.jsonJwtApi)
    implementation(CoffeeWordDepencencies.jsonJwtImpl)
    implementation(CoffeeWordDepencencies.jsonJwtJackson)

    implementation(CoffeeWordDepencencies.coroutinesCore)
    implementation(CoffeeWordDepencencies.coroutinesAndroid)

    implementation(CoffeeWordDepencencies.navigationFragment)
    implementation(CoffeeWordDepencencies.navigationUi)

    implementation(CoffeeWordDepencencies.glide)

    implementation(CoffeeWordDepencencies.roomKtx)
    implementation(CoffeeWordDepencencies.roomRuntime)
    kapt(CoffeeWordDepencencies.kaptRoom)

    implementation(CoffeeWordDepencencies.gson)

    implementation(CoffeeWordDepencencies.splashScreen)

    implementation(CoffeeWordDepencencies.swipeRefreshLayout)

    implementation(CoffeeWordDepencencies.styleableToast)
}