package com.example.core.network.model

data class NewsResponse(
    val articles: MutableList<ArticleResponse>,
    val status: String,
    val totalResults: Int
)