package com.example.lab09_puntosadicionales.data

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com/"

interface AmphibiansRepository {
    suspend fun getAmphibians(): List<Amphibian>
}

private interface AmphibiansApiService {
    @GET("amphibians")
    suspend fun getAmphibians(): ResponseBody
}

class AmphibiansContainer {
    private val service: AmphibiansApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()
            .create(AmphibiansApiService::class.java)
    }

    val repository: AmphibiansRepository by lazy { NetworkAmphibiansRepository(service) }
}

private class NetworkAmphibiansRepository(
    private val service: AmphibiansApiService,
) : AmphibiansRepository {
    override suspend fun getAmphibians(): List<Amphibian> =
        service.getAmphibians().use { response ->
            AmphibiansJson.parse(response.string())
        }
}

object AmphibiansJson {
    fun parse(body: String): List<Amphibian> =
        Json.parseToJsonElement(body).jsonArray.map { item ->
            val fields = item.jsonObject
            Amphibian(
                name = fields.getValue("name").jsonPrimitive.content,
                type = fields.getValue("type").jsonPrimitive.content,
                description = fields.getValue("description").jsonPrimitive.content,
                imageUrl = fields.getValue("img_src").jsonPrimitive.content,
            )
        }
}
