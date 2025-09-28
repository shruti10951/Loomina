package com.shrujan.loomina.ui.thread

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shrujan.loomina.data.repository.ThreadRepository
import com.shrujan.loomina.viewmodel.ThreadViewModel
import com.shrujan.loomina.viewmodel.factory.ThreadViewModelFactory

@Composable
fun ThreadDetailsScreen (
    navController: NavController,
    innerPadding: PaddingValues,
    threadId: String,
    threadViewModel: ThreadViewModel = viewModel(
        factory = ThreadViewModelFactory(
            repository = ThreadRepository(LocalContext.current))
    ),
) {
    val thread by threadViewModel.thread.collectAsState()
    val threadError by threadViewModel.error.collectAsState()

    // fetch thread when screen loads
    LaunchedEffect(threadId) {
        threadViewModel.getThreadById(threadId)
    }

    LazyColumn (
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
    }
}