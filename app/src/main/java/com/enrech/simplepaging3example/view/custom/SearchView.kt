package com.enrech.simplepaging3example.view.custom

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import com.enrech.simplepaging3example.databinding.SearchViewBinding

class SearchView(context: Context, attrs: AttributeSet): FrameLayout(context, attrs) {

    private val binding: SearchViewBinding =
        SearchViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            false
        )

    private lateinit var onSearchAction: (query: String) -> Unit

    init {
        with(binding) {
            this@SearchView.addView(root)

            searchButton.setOnClickListener { view ->
                if (::onSearchAction.isInitialized) {
                    searchEt.text?.let { text ->
                        onSearchAction(text.toString())
                    }
                    closeKeyboard(view)
                }
            }

            searchTextInput.setEndIconOnClickListener {
                searchEt.text?.clear()
                searchTextInput.setEndIconActivated(false)
                closeKeyboard(it)
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