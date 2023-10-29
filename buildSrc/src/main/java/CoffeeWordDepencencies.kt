object CoffeeWordDepencencies {

    val coreKtx by lazy { "androidx.core:core-ktx:${CoffeeWordVersions.coreKtx}" }
    val appCompat by lazy { "androidx.appcompat:appcompat:${CoffeeWordVersions.appCompat}" }
    val material by lazy { "com.google.android.material:material:${CoffeeWordVersions.material}" }
    val constraintLayout by lazy { "androidx.constraintlayout:constraintlayout:${CoffeeWordVersions.constraintLayout}" }
    val junit by lazy { "junit:junit:${CoffeeWordVersions.junit}" }
    val extJunit by lazy { "androidx.test.ext:junit:${CoffeeWordVersions.extJunit}" }
    val espressoCore by lazy { "androidx.test.espresso:espresso-core:${CoffeeWordVersions.espressoCore}" }

    val hilt by lazy { "com.google.dagger:hilt-android:${CoffeeWordVersions.hilt}" }
    val kaptHilt by lazy { "com.google.dagger:hilt-compiler:${CoffeeWordVersions.hilt}" }

    val viewModelKtx by lazy { "androidx.lifecycle:lifecycle-viewmodel-ktx:${CoffeeWordVersions.lifecycle}" }
    val liveDataKtx by lazy { "androidx.lifecycle:lifecycle-livedata-ktx:${CoffeeWordVersions.lifecycle}" }

    val retrofit by lazy { "com.squareup.retrofit2:retrofit:${CoffeeWordVersions.retrofit}" }
    val retrofitConverterGson by lazy { "com.squareup.retrofit2:converter-gson:${CoffeeWordVersions.retrofit}" }

    val okhttp3 by lazy { "com.squareup.okhttp3:okhttp:${CoffeeWordVersions.okhttp3}" }
    val okhttp3LoggingInterceptor by lazy { "com.squareup.okhttp3:logging-interceptor:${CoffeeWordVersions.okhttp3}" }

    val jsonJwtApi by lazy { "io.jsonwebtoken:jjwt-api:${CoffeeWordVersions.jsonJwt}" }
    val jsonJwtImpl by lazy { "io.jsonwebtoken:jjwt-impl:${CoffeeWordVersions.jsonJwt}" }
    val jsonJwtJackson by lazy { "io.jsonwebtoken:jjwt-jackson:${CoffeeWordVersions.jsonJwt}" }

    val coroutinesCore by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-core:${CoffeeWordVersions.coroutines}" }
    val coroutinesAndroid by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-android:${CoffeeWordVersions.coroutines}" }

    val navigationFragment by lazy { "androidx.navigation:navigation-fragment-ktx:${CoffeeWordVersions.navigation}" }
    val navigationUi by lazy { "androidx.navigation:navigation-ui-ktx:${CoffeeWordVersions.navigation}" }

    val glide by lazy { "com.github.bumptech.glide:glide:${CoffeeWordVersions.glide}" }

    val roomRuntime by lazy { "androidx.room:room-runtime:${CoffeeWordVersions.room}" }
    val roomKtx by lazy { "androidx.room:room-ktx:${CoffeeWordVersions.room}" }
    val kaptRoom by lazy { "androidx.room:room-compiler:${CoffeeWordVersions.room}" }

    val gson by lazy { "com.google.code.gson:gson:${CoffeeWordVersions.gson}" }

    val splashScreen by lazy { "androidx.core:core-splashscreen:${CoffeeWordVersions.splashScreen}" }

    val swipeRefreshLayout by lazy { "androidx.swiperefreshlayout:swiperefreshlayout:${CoffeeWordVersions.swipeRefreshLayout}" }

    val styleableToast by lazy {"io.github.muddz:styleabletoast:${CoffeeWordVersions.styleableToast}"}
}