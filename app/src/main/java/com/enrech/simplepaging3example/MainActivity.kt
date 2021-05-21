package com.enrech.simplepaging3example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.enrech.simplepaging3example.databinding.ActivityMainBinding
import com.enrech.simplepaging3example.model.Repo
import com.enrech.simplepaging3example.view.adapter.ReposAdapter
import com.enrech.simplepaging3example.view.adapter.ReposLoadStateAdapter
import com.enrech.simplepaging3example.view.viewmodel.GithubViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: GithubViewModel by viewModels()
    private var searchJob: Job? = null
    private val adapter: ReposAdapter = ReposAdapter()

    private var initialSearchPerformed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViews()
        initOnClickListeners()
    }

    private fun setUpViews() = with(binding) {
        mainRecyclerview.adapter = adapter.withLoadStateFooter(ReposLoadStateAdapter(adapter::retry))

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .collect {
                    val isEmptyList = it.refresh is LoadState.NotLoading && adapter.itemCount == 0
                    showEmptyList(isEmptyList)

                    mainRecyclerview.isVisible = it.source.refresh is LoadState.NotLoading
                    progressBar.isVisible = it.source.refresh is LoadState.Loading
                    errorViewContainer.errorView.isVisible = it.source.refresh is LoadState.Error
                }
        }

        errorViewContainer.errorRetryButton.setOnClickListener { adapter.retry() }
    }

    private fun initOnClickListeners() = with(binding) {
        searchView.addOnSearchListener { query ->
            if (initialSearchPerformed.not()) initialSearchPerformed = true
            search(query)
        }
    }

    private fun search(username: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchRepos(username).collect { adapter.submitData(it) }
        }
    }

    private fun showEmptyList(show: Boolean) = with(binding) {
        emptyViewContainer.emptyView.isVisible = show && initialSearchPerformed
    }
}