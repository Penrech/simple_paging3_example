package com.enrech.simplepaging3example.data.service

import com.enrech.simplepaging3example.model.Repo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {
    @GET("users/{username}/repos")
    suspend fun fetchRepos(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") size: Int
    ): List<Repo>
}