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
import com.shrujan.loomina.data.remote.dto.SparkResponse
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

) {

    val uiState by sparkViewModel.uiState.collectAsState()
    val currentSpark by sparkViewModel.spark.collectAsState()

    var sparkChain by remember { mutableStateOf<List<SparkResponse>>(emptyList()) }

    // Load the initial spark
    LaunchedEffect(currentSparkId) {
        sparkViewModel.getSparkById(currentSparkId)
    }

    // When currentSpark loads, add it to the chain if empty
    LaunchedEffect(currentSpark) {
        currentSpark?.let {
            if (sparkChain.isEmpty()) {
                sparkChain = listOf(it)
            }
        }
    }

    // When a new spark is created successfully, append it
    LaunchedEffect(uiState.spark) {
        uiState.spark?.let { newSpark ->
            sparkChain = sparkChain + newSpark
            sparkViewModel.resetState() // reset to clear state for next composer
        }
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        if (sparkChain.isEmpty()) {
            item {
                Text("Loading spark...")
            }
        } else {
            // Render all sparks in order
            items(sparkChain) { spark ->
                SparkItem(spark)
            }

            // Composer for next spark
            item {
                SparkComposer { text, isSensitive ->
                    sparkViewModel.createSpark(
                        threadId = threadId,
                        sparkText = text,
                        previousSparkId = sparkChain.lastOrNull()?.id,
                        isSensitive = isSensitive
                    )
                }
            }
        }

    }

}
