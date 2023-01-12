group "com.github.bkmbigo.visiozoezi"
version "1.0-SNAPSHOT"

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Plugins.PluginDependencies.sqlDelight)
        classpath(Plugins.PluginDependencies.androidGradle)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    kotlin("multiplatform") apply false
    kotlin("android") apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.compose") apply false
    id(Plugins.PluginDependencies.kotlinSerialization) version Versions.kotlin apply false
}