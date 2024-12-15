package com.example.feature.saved

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.core.model.Article
import com.example.core.ui.components.DynamicAppBar
import com.example.core.ui.components.EkaLoadingWheel
import com.example.core.ui.viewmodel.NewsViewModel
import com.example.core.ui.navigation.handle
import com.example.core.ui.theme.LocalTintTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SavedScreen(
    viewModel: NewsViewModel,
    onClick: ((Article) -> Unit)? = null,
    onShareClick: ((String) -> Unit)? = null
) {
    val savedUiState by viewModel.savedUiState.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var isSaved by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.getSavedArticles()
    }

    if (!isSaved) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar("Article removed")
        }
    }

    savedUiState?.handle(
        {
            SavedScreenWrapper(
                onClick, onShareClick, it, snackbarHostState = snackbarHostState,
                onSavedClick = { article ->
                    viewModel.unSaveArticle(article)
                    isSaved = false
                }
            )
        },
        {
            EkaLoadingWheel(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(),
                contentDesc = "Loading Saved Articles"
            )
        },
        { errorMessage ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                Text(
                    text = errorMessage ?: "An error occurred."
                )
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SavedScreenWrapper(
    onClick: ((Article) -> Unit)? = null,
    onShareClick: ((String) -> Unit)? = null,
    articles: List<Article>?,
    onSavedClick: ((Article) -> Unit),
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            DynamicAppBar(
                title = "Saved Articles",
                backgroundColor = Color.Transparent,
                contentColor = Color.White,
                titleColor = Color.Black,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            RenderSavedList(onClick, onShareClick, articles, onSavedClick)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RenderSavedList(
    onClick: ((Article) -> Unit)? = null,
    onShareClick: ((String) -> Unit)?,
    articles: List<Article>?,
    onSavedClick: ((Article) -> Unit)
) {
    if (articles.isNullOrEmpty()) {
        EmptyState()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxHeight()
        ) {
            items(articles) { article ->
                SavedNewsCard(
                    article,
                    onShareClick = { url ->
                        onShareClick?.invoke(url)
                    },
                    onSaveClick = onSavedClick,
                    onClick = { onClick?.invoke(article) }
                )
            }
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val iconTint = LocalTintTheme.current.iconTint
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = R.drawable.feature_bookmarks_img_empty_bookmarks),
            colorFilter = if (iconTint != Color.Unspecified) ColorFilter.tint(iconTint) else null,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = stringResource(id = R.string.feature_bookmarks_empty_error),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.feature_bookmarks_empty_description),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}


