import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        BrowserViewportWindow("VisioZoezi") {
            MaterialTheme {
                Text("Compose for Web")
            }
        }
    }
}

