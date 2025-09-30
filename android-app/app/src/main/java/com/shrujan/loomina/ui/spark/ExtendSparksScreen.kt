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
import com.shrujan.loomina.viewmodel.SparkViewModel
import com.shrujan.loomina.viewmodel.ThreadViewModel
import com.shrujan.loomina.viewmodel.factory.SparkViewModelFactory
import com.shrujan.loomina.viewmodel.factory.ThreadViewModelFactory

@Composable
fun ExtendSparksScreen(
    navController: NavController,
    innerPadding: PaddingValues,
    threadId: String,
    currentSparkId: String,
    sparkViewModel: SparkViewModel = viewModel(
        factory = SparkViewModelFactory(
            repository = SparkRepository(LocalContext.current)
        )
    ),
    threadViewModel: ThreadViewModel = viewModel(
        factory = ThreadViewModelFactory(
            repository = ThreadRepository(LocalContext.current))
    ),
) {

    val thread by threadViewModel.thread.collectAsState()
    val threadError by threadViewModel.error.collectAsState()

    val currentSpark by sparkViewModel.spark.collectAsState()

    // fetch thread when screen loads
    LaunchedEffect(threadId) {
        threadViewModel.getThreadById(threadId)
        sparkViewModel.getSparkById(currentSparkId)
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

        item {
            when {
                currentSpark == null -> Text("Loading current spark...")
                else -> SparkItem(
                    spark = currentSpark!!
                )
            }
        }

    }

}
