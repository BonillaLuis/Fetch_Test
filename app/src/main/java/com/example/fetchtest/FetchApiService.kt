package com.example.fetchtest

import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface FetchApiService {
    @GET("hiring.json")
    suspend fun getItems(): List<FetchList>

    companion object {
        private const val BASE_URL = "https://hiring.fetch.com/"

        fun create(): FetchApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FetchApiService::class.java)
        }
    }
}
fun processItems(rawItems: List<FetchList>): List<FetchList> {
    return rawItems
        .filter { !it.name.isNullOrBlank() }
        .sortedWith(compareBy(
            { it.listId },
            { extractNumber(it.name) }
        ))
}
fun extractNumber(name: String?): Int {
    return name?.substringAfter("Item ")?.toIntOrNull() ?: Int.MAX_VALUE
}
