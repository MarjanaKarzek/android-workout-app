package com.karzek.buildsrc

object Config {
    const val minSdk = 24
    const val targetSdk = 29
    const val versionCode = 1
    const val versionName = "1.0"
    const val buildToolsVersion = "29.0.2"
}

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:3.6.1"

    object Kotlin {
        private const val version = "1.3.61"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    }

    object AndroidArch {
        private const val version = "2.1.2"
        const val paging = "androidx.paging:paging-runtime-ktx:$version"
        const val rxJava2 = "androidx.paging:paging-rxjava2-ktx:$version"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.1.0"
        const val coreKtx = "androidx.core:core-ktx:1.2.0"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0-alpha1"
    }

    object Glide {
        private const val version = "4.11.0"
        const val glide = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object Google {
        const val material = "com.google.android.material:material:1.2.0-alpha06"
    }

    object AutoDispose {
        private const val version = "1.4.0"
        const val autoDispose = "com.uber.autodispose:autodispose:$version"
        const val android = "com.uber.autodispose:autodispose-android-archcomponents:$version"
    }

    object Chucker {
        private const val version = "3.2.0"
        const val debug = "com.github.ChuckerTeam.Chucker:library:$version"
        const val release = "com.github.ChuckerTeam.Chucker:library-no-op:$version"
    }

    object Dagger {
        private const val version = "2.27"
        const val dagger = "com.google.dagger:dagger:$version"
        const val androidSupport = "com.google.dagger:dagger-android-support:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"
        const val androidProcessor = "com.google.dagger:dagger-android-processor:$version"

        object JW {
            private const val version = "0.2.0"
            const val reflect = "com.jakewharton.dagger:dagger-reflect:$version"
            const val codegen = "com.jakewharton.dagger:dagger-codegen:$version"
        }
    }

    object Gson {
        private const val version = "2.8.6"
        const val gson = "com.google.code.gson:gson:$version"
    }

    object Intuit {
        private const val version = "1.0.6"
        const val ssp = "com.intuit.ssp:ssp-android:$version"
        const val sdp = "com.intuit.sdp:sdp-android:$version"
    }

    object LeakCanary {
        private const val version = "2.2"
        const val leakCanary = "com.squareup.leakcanary:leakcanary-android:$version"
    }

    object SquareUp {
        private const val version = "4.7.2"
        const val okHttp = "com.squareup.okhttp3:okhttp:$version"
        const val okio = "com.squareup.okio:okio:2.6.0"
        const val okhttp3mock = "com.squareup.okhttp3:mockwebserver:$version"
    }

    object ReactiveX {
        object Java {
            private const val version = "2.1.1"
            const val rxJava2Android = "io.reactivex.rxjava2:rxandroid:$version"
        }

        object Kotlin {
            private const val version = "2.4.0"
            const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:$version"
        }

        object Binding {
            private const val version = "3.1.0"
            const val binding = "com.jakewharton.rxbinding3:rxbinding:$version"
            const val appcompat = "com.jakewharton.rxbinding3:rxbinding-appcompat:$version"
            const val recyclerView = "com.jakewharton.rxbinding3:rxbinding-recyclerview:$version"
        }
    }

    object Retrofit {
        private const val version = "2.8.1"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val retrofitGson = "com.squareup.retrofit2:converter-gson:$version"
        const val retrofitRxAdapter = "com.squareup.retrofit2:adapter-rxjava2:$version"
    }

    object Room {
        private const val version = "2.2.5"
        const val room = "androidx.room:room-runtime:$version"
        const val compiler = "androidx.room:room-compiler:$version"
        const val rxJava2 = "androidx.room:room-rxjava2:$version"
    }

    object Timber {
        private const val version = "4.7.1"
        const val timber = "com.jakewharton.timber:timber:$version"
    }

    object Animator {
        private const val version = "3.0.0"
        const val recyclerViewAnimators = "jp.wasabeef:recyclerview-animators:$version"
    }

    object Test {
        object Mockk {
            private const val version = "1.9.3"
            const val mockk = "io.mockk:mockk:$version"
            const val mockkAndroid = "io.mockk:mockk-android:$version"
        }

        object JUnit {
            private const val version = "5.6.1"
            const val jupiterApi = "org.junit.jupiter:junit-jupiter-api:$version"
            const val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine:$version"
        }

        object Espresso {
            private const val version = "3.2.0"
            const val espresso = "androidx.test.espresso:espresso-core:$version"
        }

        object AndroidX {
            const val runner = "androidx.test:runner:1.2.0"
            const val test = "androidx.test:core:1.2.0"
            const val testExt = "androidx.test.ext:junit:1.1.1"
        }

        object RESTMock {
            private const val version = "0.4.3"
            const val mockServer = "com.github.andrzejchm.RESTMock:android:$version"
        }
    }
}
