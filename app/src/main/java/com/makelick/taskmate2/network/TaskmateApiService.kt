package com.makelick.taskmate2.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private val BASE_URL = "https://kanbantaskmate.up.railway.app/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface TaskmateApiService {
    @POST("api/v1/auth/oauth")
    suspend fun login(@Body code: TaskmateApiAuthCode): String

}

object TaskmateApi {
    val retrofitService: TaskmateApiService by lazy {
        retrofit.create(TaskmateApiService::class.java)
    }
}