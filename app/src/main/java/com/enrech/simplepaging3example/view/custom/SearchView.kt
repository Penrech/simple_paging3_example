package com.enrech.simplepaging3example.view.custom

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import com.enrech.simplepaging3example.databinding.SearchViewBinding
import com.google.android.material.snackbar.Snackbar

class SearchView(context: Context, attrs: AttributeSet): FrameLayout(context, attrs) {

    private val binding: SearchViewBinding =
        SearchViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            false
        )

    private lateinit var onSearchAction: (query: String) -> Unit
    lateinit var clearAction: () -> Unit

    init {
        with(binding) {
            this@SearchView.addView(root)
            
            searchButton.setOnClickListener { view ->
                searchByText(searchEt.text?.toString())
                closeKeyboard(view)
            }


            searchTextInput.setEndIconOnClickListener {
                searchEt.text?.clear()
                searchTextInput.setEndIconActivated(false)
                closeKeyboard(it)
                if (::clearAction.isInitialized) { clearAction() }
            }

            searchEt.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchByText(searchEt.text?.toString())
                }
                false
            }
        }
    }

    private fun searchByText(text: String?) {
        if (text.isNullOrBlank()) {
            Snackbar
                .make(binding.searchTextInput, "Query shouldn't be empty", Snackbar.LENGTH_LONG)
                .apply { anchorView = binding.cardView }
                .show()

        } else {
            if (::onSearchAction.isInitialized) {
                onSearchAction(text)
            }
        }
    }

    fun addOnSearchListener(callback: (query: String) -> Unit) {
        onSearchAction = callback
    }

    private fun closeKeyboard(view: View) {
        (context?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager)?.apply {
            hideSoftInputFromWindow(view.applicationWindowToken, 0)
        }
    }

}