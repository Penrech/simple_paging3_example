package com.enrech.simplepaging3example.data.service

import retrofit2.Retrofit

object ApiService {
    fun instantiate(): GitHubApiService =
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .build()
            .create(GitHubApiService::class.java)
}