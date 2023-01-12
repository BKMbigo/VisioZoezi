object Android {
    object AndroidSdk {
        const val minSdk = 24
        const val targetSdk = 33
        const val compileSdk = 33


    }

    object AndroidDependencies {

        const val coreKtx = "androidx.core:core-ktx:${Versions.androidCore}"

        const val composeUI = "androidx.compose.ui:ui:${Versions.androidCompose}"
        const val composeMaterial = "androidx.compose.material:material:${Versions.androidCompose}"
        const val composeTooling = "androidx.compose.ui:ui-tooling-preview:${Versions.androidCompose}"
        const val composeRuntime = "androidx.compose.runtime:runtime-livedata:${Versions.androidCompose}"
        const val composeUtil = "androidx.compose.ui:ui-util:${Versions.androidCompose}"
        const val composeActivity = "androidx.activity:activity-compose:${Versions.composeActivity}"
        const val material3 = "androidx.compose.material3:material3:${Versions.composeMaterial3}"

        const val accompanistPermission = "com.google.accompanist:accompanist-permissions:${Versions.accompanist}"

        const val ktorAndroid = "io.ktor:ktor-client-android:${Versions.ktor}"

        const val cameraCore = "androidx.camera:camera-core:${Versions.cameraX}"
        const val camera2 = "androidx.camera:camera-camera2:${Versions.cameraX}"
        const val cameraView = "androidx.camera:camera-view:${Versions.cameraX}"
        const val cameraLifecycle = "androidx.camera:camera-lifecycle:${Versions.cameraX}"

        const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
        const val glideCompose = "com.github.bumptech.glide:compose:${Versions.glideCompose}"

        const val tfLite = "org.tensorflow:tensorflow-lite:${Versions.tfLite}"
        const val tfLiteSupport = "org.tensorflow:tensorflow-lite-support:${Versions.tfLiteSupport}"

        const val sqlDelightAndroid = "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"

        const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"

        const val koltinDlVisualization =  "org.jetbrains.kotlinx:kotlin-deeplearning-visualization:${Versions.kotlinDl}"
        const val kotlinDlOnnx =  "org.jetbrains.kotlinx:kotlin-deeplearning-onnx:${Versions.kotlinDl}"

        object Test {
            const val jUnitKtx = "androidx.test.ext:junit-ktx:${Versions.jUnitKtx}"

            const val coroutinesTest =
                "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesTest}"
        }
    }
}