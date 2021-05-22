package com.enrech.simplepaging3example.view.custom.empty

import com.enrech.simplepaging3example.R

enum class EmptyVoEnum(
    val imageId: Int,
    val imageColorId: Int,
    val descriptionId: Int,
    val buttonTitleId: Int? = null,
    var callback: (() -> Unit)? = null
) {
    EMPTY_INITIAL_LIST(
        R.drawable.ic_round_search_24,
        R.color.purple_200,
        R.string.initial_info
    ),
    EMPTY_LIST(
        R.drawable.ic_twotone_cloud_off_24,
        R.color.purple_200,
        R.string.no_results
    ),
    EMPTY_ERROR_VIEW(
        R.drawable.ic_twotone_error_24,
        R.color.red_error,
        R.string.error,
        R.string.retry_button
    )
}