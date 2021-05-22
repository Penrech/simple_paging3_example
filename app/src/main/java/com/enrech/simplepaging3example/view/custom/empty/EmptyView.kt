package com.enrech.simplepaging3example.view.custom.empty

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.enrech.simplepaging3example.databinding.EmptyViewBinding

class EmptyView(context: Context, attrs: AttributeSet): FrameLayout(context, attrs) {

    private val binding: EmptyViewBinding =
        EmptyViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            false
        )

    init {
        addView(binding.root)
    }

    fun setView(emptyVoEnum: EmptyVoEnum) = with(binding) {
        emptyImageView.setImageResource(emptyVoEnum.imageId)
        emptyTextView.text = context.getString(emptyVoEnum.descriptionId)
        emptyVoEnum.callback?.let {
            emptyRetryButton.setOnClickListener { it() }
            emptyRetryButton.isVisible = true
        }
    }
}