package com.example.ekanews.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.core.headline.components.ArticleCard
import com.example.core.model.Article
import com.example.core.ui.navigation.UiState
import com.example.core.ui.theme.LocalTintTheme
import com.example.core.ui.utils.Utils
import com.example.core.ui.viewmodel.NewsViewModel
import com.example.ekanews.components.SearchBar
import com.example.feature.headline.R
import kotlinx.serialization.encodeToString
import java.net.URLEncoder

// TODO: Move this screen to "search" module
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchScreen(viewModel: NewsViewModel, navController: NavController, isNetworkConnected: Boolean) {
    var searchQuery by remember { mutableStateOf(viewModel.searchedText) }
    val searchUiState by viewModel.searchUiState.observeAsState()
    var searchUiNews by remember { mutableStateOf(listOf<Article>()) }

    when(searchUiState){
        is UiState.Loading -> {
            // Nothing as of now
        }
        is UiState.Success -> {
            val searchedArticles = (searchUiState as UiState.Success).articles
            searchUiNews = searchedArticles ?: listOf()
        }
        is UiState.Error -> {
            val errorMessage = (searchUiState as UiState.Error).errorMessage
        }
        else -> {
        }
    }

    if(!isNetworkConnected){
        NoInternetState()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChanged = {
                    searchQuery = it
                    if (it.isEmpty()) {

                    }
                    viewModel.searchNews(it)
                    viewModel.searchedText = it
                }
            )
//
            LazyColumn {
                items(searchUiNews){article ->
                    ArticleCard(
                        title = article.title ?: "No Headline Available",
                        description = article.description ?: "No Description",
                        imageUrl = article.urlToImage
                            ?: "https://upload.wikimedia.org/wikipedia/commons/1/1d/Bharat_Times_News_logo.jpg",
                        date = article.publishedAt?.let { Utils.formatDate(it) } ?: "Unknown Date",
                        tag = article.source ?: "Unknown Source",
                        onClick = {
                            val json = kotlinx.serialization.json.Json.encodeToString(article)
                            navController.navigate("headLineDetail/${URLEncoder.encode(json, "UTF-8")}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NoInternetState(modifier: Modifier = Modifier) {
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
            text = stringResource(id = R.string.no_internet),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.connect_internet_to_search_news),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
