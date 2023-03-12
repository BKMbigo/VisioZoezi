import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id(Plugins.PluginName.sqlDelight)
    id(Plugins.PluginName.kotlinxSerialization)
}

group = "com.github.bkmbigo.visiozoezi"
version = "1.0-SNAPSHOT"

kotlin {
    android()
    js(IR) {
        browser()
        binaries.executable()
    }
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    sourceSets {
        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.material3)
                api(compose.ui)
                api(compose.animation)

                api(compose.materialIconsExtended)

                api(Common.kotlinxDateTime)

                api(Common.ktorCore)
                api(Common.ktorLogging)
                api(Common.ktorSerialization)
                api(Common.ktorContentNegotiation)
                api(Common.ktorSerializationJson)

                api(Common.sqlDelight)
                api(Common.sqlDelightCoroutine)

                api(Common.multiplatformSettings)

            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)

                implementation(Javascript.routingCompose)

                implementation(Javascript.ktorClientJS)

                implementation(Javascript.sqlDelightJs)

                implementation(npm("sql.js", "1.6.2"))
                implementation(devNpm("copy-webpack-plugin", "9.1.0"))
            }
        }
        val jvmMain by creating {
            dependsOn(commonMain)
            dependencies {

                api(compose.uiTooling)

                api(Common.compooseAnimatedImages)

                api(Common.voyager)
                api(Common.voyagerTransitions)
            }
        }

        val androidMain by getting {
            dependsOn(jvmMain)
            dependencies {
                api("androidx.appcompat:appcompat:1.5.1")
                api(Android.AndroidDependencies.coreKtx)

                api(Android.AndroidDependencies.composeRuntime)
                api(Android.AndroidDependencies.composeTooling)
                api(Android.AndroidDependencies.composeUI)
                api(Android.AndroidDependencies.composeUtil)

                api(Android.AndroidDependencies.accompanistPermission)

                api(Android.AndroidDependencies.camera2)
                api(Android.AndroidDependencies.cameraCore)
                api(Android.AndroidDependencies.cameraLifecycle)
                api(Android.AndroidDependencies.cameraView)

                api(Android.AndroidDependencies.ktorAndroid)

                api(Android.AndroidDependencies.glide)
                api(Android.AndroidDependencies.glideCompose)

                api(Android.AndroidDependencies.sqlDelightAndroid)

                api(Android.AndroidDependencies.kotlinDlOnnx)
                api(Android.AndroidDependencies.koltinDlVisualization)

                api(Android.AndroidDependencies.tfLite)
                api(Android.AndroidDependencies.tfLiteSupport)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
                implementation(Desktop.sqlDelightJvm)
            }
        }
        val desktopMain by getting {
            dependsOn(jvmMain)
            dependencies {
                api(compose.preview)
                api(compose.desktop.currentOs)

                api(Desktop.ktorJvm)

                api(Desktop.sqlDelightJvm)

                api(Desktop.webcamCapture)

                api(Desktop.deepLearningApi)
                api(Desktop.deepLearningTensorflowEngine)
                api(Desktop.deepLearningTensorflowModelZoo)

                api(Desktop.kotlinDlTensorflow)
            }
        }
        val desktopTest by getting
    }
}

compose.experimental {
    web.application {}
}

android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        compose = true
    }
}

sqldelight {
    this.databases {
        create("VisioZoeziDatabase") {
            packageName.set("com.github.bkmbigo.visiozoezi.common.data.persistence")
            generateAsync.set(true)
        }
    }
}