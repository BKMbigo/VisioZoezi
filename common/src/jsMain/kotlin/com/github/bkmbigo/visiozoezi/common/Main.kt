package com.github.bkmbigo.visiozoezi.common

import androidx.compose.material3.Text
import com.github.bkmbigo.visiozoezi.common.presentation.theme.VisioZoeziTheme
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        BrowserViewportWindow {
            VisioZoeziTheme {
                Text("Welcome to Compose Web")
            }
        }
    }
}