package com.github.bkmbigo.visiozoezi.common.data.network.api

import exerciseDbApiKey
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse

class ExerciseDbApi(
    private val client: HttpClient = ktorClient
) {

    suspend fun getAllExercises(): HttpResponse {
        return client.get("$HOST_URL${ApiRoutes.ALL_EXERCISES}") {
            url {
                parameters.append("api_key", exerciseDbApiKey)
            }
            attachHeaders()
        }
    }

    suspend fun getAllEquipment(): HttpResponse {
        return client.get("$HOST_URL${ApiRoutes.ALL_EQUIPMENT}") {
            url {
                parameters.append("api_key", exerciseDbApiKey)
            }
            attachHeaders()
        }
    }

    suspend fun getAllBodyPart(): HttpResponse {
        return client.get("$HOST_URL${ApiRoutes.ALL_BODY_PARTS}") {
            url {
                parameters.append("api_key", exerciseDbApiKey)
            }
            attachHeaders()
        }
    }

    suspend fun getAllTargetMuscles(): HttpResponse {
        return client.get("$HOST_URL${ApiRoutes.ALL_TARGET_MUSCLES}") {
            url {
                parameters.append("api_key", exerciseDbApiKey)
            }
            attachHeaders()
        }
    }


    companion object {
        const val HOST_URL = "https://exercisedb.p.rapidapi.com/"

        object ApiRoutes {
            const val ALL_EXERCISES = "exercises"
            const val ALL_EQUIPMENT = "exercises/equipmentList"
            const val ALL_BODY_PARTS = "exercises/bodyPartList"
            const val ALL_TARGET_MUSCLES = "exercises/targetList"
        }

        fun HttpRequestBuilder.attachHeaders() {
            headers.clear()
            headers {
                headers.append(
                    "X-RapidAPI-Key",
                    exerciseDbApiKey
                ) // API Key obscured from version control
                headers.append("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            }
        }
    }
}