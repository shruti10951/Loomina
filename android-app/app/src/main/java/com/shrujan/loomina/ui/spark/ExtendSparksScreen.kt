package com.shrujan.loomina.ui.spark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shrujan.loomina.data.repository.SparkRepository
import com.shrujan.loomina.data.repository.ThreadRepository
import com.shrujan.loomina.ui.thread.ThreadHeader
import com.shrujan.loomina.viewmodel.CreateSparkViewModel
import com.shrujan.loomina.viewmodel.ThreadViewModel
import com.shrujan.loomina.viewmodel.factory.SparkViewModelFactory
import com.shrujan.loomina.viewmodel.factory.ThreadViewModelFactory

@Composable
fun AddSparksScreen(
    navController: NavController? = null, // optional for testing
    innerPadding: PaddingValues,
    threadId: String,
    createSparkViewModel: CreateSparkViewModel = viewModel(
        factory = SparkViewModelFactory(
            repository = SparkRepository(LocalContext.current)
        )
    ),
    threadViewModel: ThreadViewModel = viewModel(
        factory = ThreadViewModelFactory(
            repository = ThreadRepository(LocalContext.current))
    ),
    onSparkAdded: () -> Unit = {}
) {
    val uiState by createSparkViewModel.uiState.collectAsState()

    val thread by threadViewModel.thread.collectAsState()
    val threadError by threadViewModel.error.collectAsState()

    // fetch thread when screen loads
    LaunchedEffect(threadId) {
        threadViewModel.getThreadById(threadId)
    }

    // Local list of spark items
    var sparkItems by remember {
        mutableStateOf<List<SparkUiItem>>(listOf(SparkUiItem.Composer(parentId = null)))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Thread header on top
        item {
            when {
                threadError != null -> Text(
                    text = threadError!!,
                    color = MaterialTheme.colorScheme.error
                )
                thread == null -> Text("Loading thread…")
                else -> ThreadHeader(thread = thread!!)
            }
        }

        // Spark list
        items(sparkItems) { item ->
            when (item) {
                is SparkUiItem.Spark -> {
                    SparkItem(
                        spark = item.data,
                        onLikeClick = { /* TODO */ },
                        onCommentClick = { /* TODO */ },
                        onExtendClick = {
                            // Add composer below this spark
                            sparkItems = sparkItems.toMutableList().apply {
                                val idx = indexOf(item)
                                add(idx + 1, SparkUiItem.Composer(parentId = item.data.id))
                            }
                        }
                    )
                }

                is SparkUiItem.Composer -> {
                    SparkComposer(
                        onPost = { text, isSensitive ->
                            createSparkViewModel.createSpark(
                                threadId = threadId,
                                sparkText = text,
                                isSensitive = isSensitive,
                                previousSparkId = item.parentId
                            )
                        }
                    )
                }
            }
        }
    }

    // Handle success → replace composer with spark
    uiState.spark?.let { newSpark ->
        LaunchedEffect(newSpark.id) {
            sparkItems = sparkItems.map { item ->
                if (item is SparkUiItem.Composer && item.parentId == newSpark.previousSparkId) {
                    SparkUiItem.Spark(newSpark)
                } else item
            }
            onSparkAdded()
            createSparkViewModel.resetState()
        }
    }

    // Show global error (if any)
    uiState.error?.let { errorMsg ->
        Text(
            text = errorMsg,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )
    }
}
