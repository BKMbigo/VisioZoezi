@file:Suppress(
    "INVISIBLE_MEMBER",
    "INVISIBLE_REFERENCE",
    "EXPOSED_PARAMETER_TYPE",
)

package com.github.bkmbigo.visiozoezi.common

import androidx.compose.ui.window.ComposeWindow
import androidx.compose.runtime.Composable
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLHeadElement
import org.w3c.dom.HTMLStyleElement

private const val CANVAS_ELEMENT_ID = "ComposeTarget"

fun BrowserViewportWindow(
    title: String = "VisioZoezi",
    content: @Composable ComposeWindow.() -> Unit
) {
    val htmlHeadElement = document.head!!
    htmlHeadElement.appendChild(
        (document.createElement("style") as HTMLStyleElement).apply {
            type = "text/css"
            appendChild(
                document.createTextNode(
                    """
                        html, body {
                            overflow: hidden;
                            margin: 0 !important;
                            padding: 0 !important;
                        }
                        
                        #$CANVAS_ELEMENT_ID {
                            outline: none;
                        }
                    """.trimIndent()
                )
            )
        }
    )

    fun HTMLCanvasElement.fillViewportSize() {
        setAttribute("width", "${window.innerWidth}")
        setAttribute("height", "${window.innerHeight}")
    }

    val canvas = (document.getElementById(CANVAS_ELEMENT_ID) as HTMLCanvasElement).apply {
        fillViewportSize()
    }

    ComposeWindow().apply {
        window.addEventListener("resize", {
            canvas.fillViewportSize()
            layer.layer.attachTo(canvas)
            val scale = layer.layer.contentScale
            layer.setSize((canvas.width / scale).toInt(), (canvas.height / scale).toInt())
            layer.layer.needRedraw()
        })

        val htmlTitleElement = (
                htmlHeadElement.getElementsByTagName("title").item(0)
                    ?: document.createElement("title").also { htmlHeadElement.appendChild(it) }
                ) as HTMLHeadElement
        htmlHeadElement.textContent = title

        setContent {
            content(this)
        }
    }
}