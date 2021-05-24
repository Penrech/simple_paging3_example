package com.enrech.simplepaging3example

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.enrech.simplepaging3example.databinding.ActivityMainBinding
import com.enrech.simplepaging3example.view.adapter.ReposAdapter
import com.enrech.simplepaging3example.view.adapter.ReposLoadStateAdapter
import com.enrech.simplepaging3example.view.custom.empty.EmptyVoEnum
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
                    showErrorView(it.source.refresh is LoadState.Error)
                }
        }

        searchView.clearAction = { clearData() }
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

    private fun clearData() {
        searchJob?.cancel()
        viewModel.clearList()
        backToInitialUI()
    }

    private fun backToInitialUI() = with(binding) {
        initialSearchPerformed = false
        mainRecyclerview.isVisible = false
        progressBar.isVisible = false
        showEmptyList(true)
    }

    private fun showEmptyList(show: Boolean) = with(binding) {
        when {
            show && initialSearchPerformed -> emptyViewContainer.setView(EmptyVoEnum.EMPTY_LIST)
            show && initialSearchPerformed.not() ->  emptyViewContainer.setView(EmptyVoEnum.EMPTY_INITIAL_LIST)
        }
        emptyViewContainer.setVisible(show, EmptyVoEnum.EMPTY_LIST, EmptyVoEnum.EMPTY_INITIAL_LIST)
    }

    private fun showErrorView(show: Boolean) = with(binding) {
        if (show) {
            emptyViewContainer.setView(
                EmptyVoEnum.EMPTY_ERROR_VIEW.apply {
                    callback = { adapter.retry() }
                }
            )
        }
        emptyViewContainer.setVisible(show, EmptyVoEnum.EMPTY_ERROR_VIEW)
    }

}