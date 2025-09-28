package com.shrujan.loomina.ui.spark

import com.shrujan.loomina.data.remote.dto.SparkResponse

sealed class SparkUiItem {
    data class Spark(val data: SparkResponse) : SparkUiItem()
    data class Composer(val parentId: String?) : SparkUiItem()
}
