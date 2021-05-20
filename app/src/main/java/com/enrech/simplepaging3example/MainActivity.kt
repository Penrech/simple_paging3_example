package com.enrech.simplepaging3example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.enrech.simplepaging3example.view.adapter.ReposAdapter
import com.enrech.simplepaging3example.view.viewmodel.GithubViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: GithubViewModel by viewModels()
    private var searchJob: Job? = null
    private val adapter: ReposAdapter = ReposAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun search(username: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchRepos(username).collect { adapter.submitData(it) }
        }
    }
}