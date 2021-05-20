package com.enrech.simplepaging3example.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.enrech.simplepaging3example.data.repository.GithubRepository
import com.enrech.simplepaging3example.model.Repo
import kotlinx.coroutines.flow.Flow

class GithubViewModel(
    private val repository: GithubRepository
) : ViewModel() {

    private var currentUsernameValue: String? = null

    private var currentSearchResult: Flow<PagingData<Repo>>? = null

    fun searchRepos(username: String): Flow<PagingData<Repo>> {
        val lastResult = currentSearchResult
        if (username == currentUsernameValue && lastResult != null) {
            return lastResult
        }
        currentUsernameValue = username
        val newResult = repository.searchRepos(username)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}