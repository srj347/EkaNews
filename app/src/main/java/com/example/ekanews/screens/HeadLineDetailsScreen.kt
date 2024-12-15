package com.example.ekanews.screens

import android.app.Activity
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.data.repository.NewsLocalRepository
import com.example.core.data.repository.NewsRemoteRepository
import com.example.core.database.ArticleDatabase
import com.example.core.model.Article
import com.example.core.network.util.RetrofitInstance
import com.example.core.ui.components.AppBarAdditionalIcons
import com.example.core.ui.components.DynamicAppBar
import com.example.core.ui.components.EkaLoadingWheel
import com.example.core.ui.localization.Localization
import com.example.core.ui.utils.Utils
import com.example.core.ui.viewmodel.NewsViewModel
import com.example.core.ui.viewmodel.NewsViewModelProviderFactory

// TODO: Move this screen to "headlinedetail" module
@Composable
fun HeadLineDetailsScreen(
    context: Activity,
    onBackClick: () -> Unit,
    item: Article?
) {
    // TODO: Move viewmodel logic out from here and update the article save logic
    val savedArticleDao = ArticleDatabase.invoke(context).getSavedArticleDao()
    val viewModel: NewsViewModel = viewModel(
        factory = NewsViewModelProviderFactory(
            NewsRemoteRepository(RetrofitInstance.api, ArticleDatabase.invoke(context).getArticleDao()),
            NewsLocalRepository(savedArticleDao)
        )
    )

    var isLoading by remember { mutableStateOf(true) }
    var isSaved by remember { mutableStateOf(false) }

    LaunchedEffect(item) {
        item?.let {
            it.id?.let { id ->
                isSaved = savedArticleDao.isArticleSaved(id)
            }
        }
    }

    Scaffold(
        topBar = {
            DynamicAppBar(
                title = item?.title ?: "Article",
                backgroundColor = Color.Transparent,
                contentColor = Color.White,
                titleColor = Color.Black,
                onNavigationClick = onBackClick,
                actions = {
                    AppBarAdditionalIcons(
                        onShareIconClicked = {
                            Utils.shareContent(context, item?.url ?: Localization.NEWS_NOT_AVAILABLE)
                        },
                        onSaveIconClicked = {
                            item?.let {
                                if (isSaved) {
                                    viewModel.unSaveArticle(it)
                                } else {
                                    viewModel.saveArticle(it)
                                }
                                isSaved = !isSaved
                            }
                        },
                        isSaved = isSaved
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            item?.url?.let { url ->
                WebViewComponent(url = url, isLoading = isLoading) {
                    isLoading = false
                }
            } ?: run {
                Text("No URL available for this article")
            }

            if (isLoading) {
                EkaLoadingWheel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(),
                    contentDesc = "Loading Article"
                )
            }
        }
    }
}

@Composable
fun WebViewComponent(url: String, isLoading: Boolean, onPageFinished: () -> Unit) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        onPageFinished()
                    }
                }
                loadUrl(url)
            }
        }
    )
}

