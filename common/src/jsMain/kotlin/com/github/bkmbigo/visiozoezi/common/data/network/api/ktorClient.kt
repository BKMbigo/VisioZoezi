package com.github.bkmbigo.visiozoezi.common.data.network.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js

actual val ktorClient: HttpClient = HttpClient(Js)