object Plugins {
    object PluginName {
        const val sqlDelight = "app.cash.sqldelight"
        const val kotlinxSerialization = "kotlinx-serialization"
    }

    object PluginDependencies {
        const val sqlDelight = "app.cash.sqldelight:gradle-plugin:${Versions.sqlDelight}"
        const val kotlinSerialization = "org.jetbrains.kotlin.plugin.serialization"
        const val androidGradle = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
    }
}