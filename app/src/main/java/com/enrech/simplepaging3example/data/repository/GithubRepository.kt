package com.enrech.simplepaging3example.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.enrech.simplepaging3example.data.datasource.GithubRepoPagingSource
import com.enrech.simplepaging3example.data.service.GithubApi

class GithubRepository(
    private val api: GithubApi
) {
    fun searchRepos(username: String) = Pager(
        pagingSourceFactory = { GithubRepoPagingSource(api, username) },
        config = PagingConfig(
            pageSize = 20
        )
    ).flow
}