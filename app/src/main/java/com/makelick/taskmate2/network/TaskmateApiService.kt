package com.makelick.taskmate2.network

import com.makelick.taskmate2.model.Board
import com.makelick.taskmate2.model.BoardCreation
import com.makelick.taskmate2.model.IssueCreation
import com.makelick.taskmate2.model.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

val logging = okhttp3.logging.HttpLoggingInterceptor().apply {
    setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY)
}

val httpClient = okhttp3.OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

private val BASE_URL = "https://kanbantaskmate.up.railway.app/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(httpClient)
    .build()

interface TaskmateApiService {
    @POST("api/v1/auth/oauth/id-token")
    suspend fun login(
        @Body idToken: Map<String, String>
    ): Map<String, String>

    @GET("api/v1/boards")
    suspend fun getBoards(
        @Header("Authorization") token: String
    ): List<Board>

    @POST("api/v1/boards")
    suspend fun createBoard(
        @Header("Authorization") token: String,
        @Body board: BoardCreation
    )

    @GET("api/v1/users/{userId}")
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): User

    @GET("api/v1/boards/{boardId}")
    suspend fun getFullBoard(
        @Header("Authorization") token: String,
        @Path("boardId") boardId: Int
    ): Board

    @PUT("api/v1/boards/{boardId}")
    suspend fun updateBoard(
        @Header("Authorization") token: String,
        @Path("boardId") boardId: Int,
        @Body board: BoardCreation
    )

    @DELETE("api/v1/boards/{boardId}")
    suspend fun deleteBoard(
        @Header("Authorization") token: String,
        @Path("boardId") boardId: Int
    ) : Response<Unit>

    @POST("api/v1/issues/boards/{boardId}")
    suspend fun createIssue(
        @Header("Authorization") token: String,
        @Path("boardId") boardId: Int,
        @Body issue: IssueCreation
    )

    @PUT("api/v1/issues/{issueId}")
    suspend fun updateIssue(
        @Header("Authorization") token: String,
        @Path("issueId") issueId: Int,
        @Body issue: IssueCreation
    )

    @DELETE("api/v1/issues/{issueId}")
    suspend fun deleteIssue(
        @Header("Authorization") token: String,
        @Path("issueId") issueId: Int
    )
}

object TaskmateApi {
    val retrofitService: TaskmateApiService by lazy {
        retrofit.create(TaskmateApiService::class.java)
    }
}