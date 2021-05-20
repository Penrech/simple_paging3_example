package com.enrech.simplepaging3example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.enrech.simplepaging3example.databinding.ActivityMainBinding
import com.enrech.simplepaging3example.model.Repo
import com.enrech.simplepaging3example.view.adapter.ReposAdapter
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
        mainRecyclerview.adapter = adapter
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
}