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
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: GithubViewModel by viewModels()
    private var searchJob: Job? = null
    private val adapter: ReposAdapter = ReposAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViews()
        initOnClickListeners()
    }

    private fun setUpViews() = with(binding) {
        mainRecyclerview.adapter = adapter.withLoadStateFooter(ReposLoadStateAdapter(adapter::retry))
//                adapter.apply {
//            withLoadStateFooter(ReposLoadStateAdapter(adapter::retry))
//            addLoadStateListener { loadState ->
//                val isEmptyList = loadState.refresh is LoadState.NotLoading && itemCount == 0
//                showEmptyList(isEmptyList)
//
//                mainRecyclerview.isVisible = loadState.source.refresh is LoadState.NotLoading
//                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
//                errorViewContainer.errorView.isVisible = loadState.source.refresh is LoadState.Error
//            }
//        }
        errorViewContainer.errorRetryButton.setOnClickListener { adapter.retry() }
    }

    private fun initOnClickListeners() = with(binding) {
        searchView.addOnSearchListener { query ->
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
        emptyViewContainer.emptyView.isVisible = show
    }
}