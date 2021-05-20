package com.enrech.simplepaging3example.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.enrech.simplepaging3example.data.service.GithubApi
import com.enrech.simplepaging3example.model.Repo

private const val INITIAL_PAGE = 1

class GithubRepoPagingSource(
    private val api: GithubApi,
    private val username: String
) : PagingSource<Int, Repo>() {

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        return try {
            val page = params.key ?: INITIAL_PAGE
            val response = api.fetchRepos(username, page, params.loadSize)
            LoadResult.Page(
                data = response,
                prevKey = if (page == INITIAL_PAGE) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}