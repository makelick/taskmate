package com.makelick.taskmate2.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.POST

private val BASE_URL = "http://kanban-taskmate.up.railway.app"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface TaskmateApiService {
    @POST("api/v1/auth/oauth")
    suspend fun login(): String

}

object TaskmateApi {
    val retrofitService: TaskmateApiService by lazy {
        retrofit.create(TaskmateApiService::class.java)
    }
}