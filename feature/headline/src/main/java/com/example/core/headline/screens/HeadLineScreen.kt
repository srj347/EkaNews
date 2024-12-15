package com.example.core.headline.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.core.headline.components.ArticleCard
import com.example.core.model.Article
import com.example.core.ui.components.DynamicAppBar
import com.example.core.ui.components.EkaLoadingWheel
import com.example.core.ui.localization.Localization
import com.example.core.ui.navigation.handle
import com.example.core.ui.theme.LocalTintTheme
import com.example.core.ui.utils.Utils
import com.example.core.ui.viewmodel.NewsViewModel
import com.example.feature.headline.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HeadLineScreen(
    viewModel: NewsViewModel,
    navController: NavController,
    onClick: ((Article) -> Unit)? = null,
    isNetworkConnected: Boolean
) {
    val TAG = "HeadLineScreen"
    val headlineUiState by viewModel.headlineUiState.observeAsState()
    var isRefresh by remember { mutableStateOf(false) }
    var articles by remember { mutableStateOf<List<Article>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var noNewsAvailable by remember { mutableStateOf(false) }
    var lastNetworkState by remember { mutableStateOf(isNetworkConnected) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isNetworkConnected) {
        if (isNetworkConnected != lastNetworkState) {
            lastNetworkState = isNetworkConnected
            coroutineScope.launch {
                delay(1000)
                var message = if(articles.isEmpty()) Localization.NO_INTERNET_CONNECTION else Localization.LOCAL_NEWS
                if (isNetworkConnected){
                    message = Localization.BACK_ONLINE
                    delay(500)
                    viewModel.getBreakingNews()
                }
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    LaunchedEffect(headlineUiState) {
        headlineUiState?.handle(
            { fetchedArticles ->
                isRefresh = false
                isLoading = false
                if (fetchedArticles.isNullOrEmpty()) {
                    noNewsAvailable = true
                } else {
                    noNewsAvailable = false
                    articles = fetchedArticles
                }
            },
            {   isLoading = true
                Log.d(TAG, "Headlines Loading..")
            },
            { error ->
                isLoading = false
                isRefresh = false
                noNewsAvailable = true
                Log.d(TAG, "Error fetching the headline: $error")
            }
        )
    }

    if (isLoading) {
        EkaLoadingWheel(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(),
            contentDesc = "Loading Headlines"
        )
    } else {
        if(noNewsAvailable){
            EmptyState()
        } else {
            HeadlineScreenWrapper(
                navController = navController,
                onClick = onClick,
                isRefreshing = isRefresh,
                onRefresh = {
                    isRefresh = true
                    viewModel.getBreakingNews()
                },
                articles = articles,
                snackbarHostState = snackbarHostState
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeadlineScreenWrapper(
    navController: NavController,
    onClick: ((Article) -> Unit)? = null,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    articles: List<Article>,
    snackbarHostState: SnackbarHostState
){
    val pullToRefreshState = rememberPullToRefreshState()
    Scaffold(
        topBar = {
            DynamicAppBar(
                title = "Latest Headlines",
                backgroundColor = Color.Transparent,
                contentColor = Color.White,
                titleColor = Color.Black,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Transparent)
                .nestedScroll(pullToRefreshState.nestedScrollConnection),
            contentAlignment = Alignment.Center
        ) {
            ArticleList(
                articles = articles,
                navController = navController,
                onClick = onClick
            )

            PullToRefreshContainer(
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshContainer(
    state: PullToRefreshState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            state.startRefresh()
        } else {
            state.endRefresh()
        }
    }

    if (state.isRefreshing) {
        LaunchedEffect(true) { onRefresh() }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ArticleList(
    articles: List<Article>,
    navController: NavController,
    onClick: ((Article) -> Unit)? = null
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = articles,
            key = { article -> article.id ?: article.url ?: UUID.randomUUID().toString() }
        ) { article ->
            ArticleCard(
                title = article.title ?: "No Headline Available",
                description = article.description ?: "",
                imageUrl = article.urlToImage
                    ?: "https://upload.wikimedia.org/wikipedia/commons/1/1d/Bharat_Times_News_logo.jpg",
                date = article.publishedAt?.let { Utils.formatDate(it) } ?: "Unknown Date",
                tag = article.source ?: "Unknown Source",
                onClick = {
                    onClick?.invoke(article)
                    val json = URLEncoder.encode(Json.encodeToString(article), "UTF-8")
                    navController.navigate(
                        "headLineDetail/${
                            json
                        }"
                    )
                }
            )
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
            painter = painterResource(id = R.drawable.img_placeholder_news),
            colorFilter = if (iconTint != Color.Unspecified) ColorFilter.tint(iconTint) else null,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = stringResource(id = R.string.no_news),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.please_try_again),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}